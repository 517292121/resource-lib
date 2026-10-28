(function () {
  'use strict';
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};

  var dashboard = document.querySelector('.db-dashboard');
  if (!dashboard) return;
  var bars = document.getElementById('db-bars');
  var tooltip = document.getElementById('db-bar-tooltip');
  var exportButton = document.getElementById('db-export');
  var refreshButton = document.getElementById('db-refresh');
  var updateState = document.getElementById('db-update-state');
  var data, loading = false;

  function number(value) { return value == null ? '—' : Number(value).toLocaleString('zh-CN'); }
  function element(tag, className, text) {
    var node = document.createElement(tag);
    if (className) node.className = className;
    if (text != null) node.textContent = text;
    return node;
  }
  function ceiling(values) {
    var peak = Math.max.apply(Math, values.concat([4]));
    var unit = Math.pow(10, Math.floor(Math.log10(peak / 4)));
    return Math.ceil(peak / 4 / unit) * unit * 4;
  }
  function axis(max) {
    var node = element('div', 'db-live-axis');
    for (var i = 4; i >= 0; i--) node.appendChild(element('span', '', number(Math.round(max * i / 4))));
    return node;
  }
  function grid() {
    var node = element('div', 'db-chart-grid');
    node.setAttribute('aria-hidden', 'true');
    for (var i = 0; i < 4; i++) {
      var line = element('i'); line.style.setProperty('--line-position', i * 25 + '%'); node.appendChild(line);
    }
    return node;
  }
  // Monotone cubic interpolation keeps sparse real counts from dipping below zero.
  function curve(points) {
    var slopes = [], tangents = [], path = 'M' + points[0].join(' ');
    for (var i = 0; i < points.length - 1; i++) slopes.push((points[i + 1][1] - points[i][1]) / (points[i + 1][0] - points[i][0]));
    tangents[0] = slopes[0]; tangents[points.length - 1] = slopes[slopes.length - 1];
    for (var j = 1; j < points.length - 1; j++) tangents[j] = slopes[j - 1] * slopes[j] <= 0 ? 0 : 2 / (1 / slopes[j - 1] + 1 / slopes[j]);
    for (var k = 0; k < points.length - 1; k++) {
      var a = points[k], b = points[k + 1], step = (b[0] - a[0]) / 3;
      path += ' C' + [a[0] + step, a[1] + step * tangents[k], b[0] - step, b[1] - step * tangents[k + 1], b[0], b[1]].join(' ');
    }
    return path;
  }
  function growthChart(allRows) {
    var rows = allRows.slice(-9);
    var chart = document.getElementById('db-trend'); chart.replaceChildren();
    if (!rows.length) { chart.appendChild(element('p', 'db-data-message', uiText('暂无文献增长数据'))); return; }
    var max = ceiling(rows.map(function (row) { return row.count; }));
    chart.appendChild(axis(max));
    var plot = element('div', 'db-trend-plot'); plot.appendChild(grid());
    var svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    svg.setAttribute('viewBox', '0 0 1000 230'); svg.setAttribute('preserveAspectRatio', 'none'); svg.setAttribute('aria-hidden', 'true');
    var points = rows.map(function (row, i) { return [i * 1000 / Math.max(1, rows.length - 1), 230 - row.count / max * 230]; });
    var line = rows.length > 1 ? curve(points) : 'M0 ' + points[0][1] + ' L1000 ' + points[0][1];
    [['db-live-area', line + ' L1000 230 L0 230 Z'], ['db-line', line]].forEach(function (item) {
      var path = document.createElementNS(svg.namespaceURI, 'path'); path.setAttribute('class', item[0]); path.setAttribute('d', item[1]); svg.appendChild(path);
    });
    plot.appendChild(svg);
    var years = element('div', 'db-live-years');
    var targets = element('div', 'db-trend-targets'); targets.style.setProperty('--point-count', rows.length);
    rows.forEach(function (row) {
      years.appendChild(element('span', '', row.year));
      var point = element('button', 'db-trend-point'); point.type = 'button';
      point.setAttribute('aria-label', uiText('{year}年新增文献 {count} 本', {year:row.year, count:number(row.count)}));
      point.appendChild(element('span', '', uiText('{year}年 · {count} 本', {year:row.year, count:number(row.count)}))); targets.appendChild(point);
    });
    plot.append(targets, years); chart.appendChild(plot);
    chart.setAttribute('aria-label', uiText('{start}年至{end}年已发布文献入库数量', {start:rows[0].year, end:rows[rows.length - 1].year}));
  }
  function hotwords(words) {
    var container = document.getElementById('db-hotwords'); container.replaceChildren();
    words.slice(0, 20).forEach(function (word, index) {
      var link = element('a', 'db-word ' + (index === 0 ? 'db-word-center' : 'db-word-' + index));
      link.href = dashboard.dataset.base + 'search?' + new URLSearchParams({ key: word });
      link.title = uiText('热门检索第 {rank} 名：{word}', {rank:index + 1, word:word});
      link.appendChild(element('span', 'db-word-text', word)); container.appendChild(link);
    });
    if (!words.length) container.appendChild(element('p', 'db-data-message', uiText('暂无热门检索词')));
  }
  function accessChart(result) {
    var rows = result.access, max = ceiling(rows.map(function (row) { return row.count; }));
    var yAxis = document.getElementById('db-access-axis');
    yAxis.replaceChildren.apply(yAxis, Array.from(axis(max).children)); bars.replaceChildren(grid());
    rows.forEach(function (row) {
      var bar = element('button', 'db-bar-item'); bar.type = 'button';
      bar.dataset.month = uiText('{year}年{month}月', {year:result.year, month:row.month}); bar.dataset.count = row.count;
      bar.setAttribute('aria-label', uiText('{month}：{count}次访问', {month:bar.dataset.month, count:number(row.count)}));
      var fill = element('i'); fill.style.setProperty('--bar', row.count / max * 100 + '%');
      bar.append(fill, element('span', '', uiText('{month}月', {month:row.month}))); bars.appendChild(bar);
    });
    var source = result.accessSource === 'site' ? uiText('站点访问量统计') : uiText('特色库资源访问量统计');
    var total = rows.reduce(function (sum, row) { return sum + row.count; }, 0);
    var description = uiText('{year}年 · {source} · 全年合计 {count} 次', {year:result.year, source:source, count:number(total)});
    document.getElementById('db-access-source').textContent = description;
    document.getElementById('db-access-title').title = description;
    bars.parentNode.setAttribute('aria-label', description);
  }
  function change(id, count, rate, unit) {
    var node = document.getElementById(id); node.replaceChildren();
    if (count == null) { node.textContent = uiText('实时统计'); return; }
    node.appendChild(element('span', '', uiText('今日增加')));
    var arrow = element('b', rate != null && rate < 0 ? 'is-down' : '', rate != null && rate < 0 ? '▼' : '▲');
    arrow.setAttribute('aria-hidden', 'true'); node.appendChild(arrow);
    node.appendChild(element('span', '', rate == null ? number(count) + unit : Math.abs(rate).toLocaleString('zh-CN', { maximumFractionDigits: 1 }) + '%'));
    node.title = uiText('今日新增 {count}{unit}', {count:number(count), unit:unit}) + (rate == null ? '' : uiText('，较昨日{change}{rate}%', {change:rate < 0 ? uiText('减少') : uiText('增长'), rate:Math.abs(rate).toFixed(1)}));
  }
  function latest(items) {
    var list = document.getElementById('db-news'); list.replaceChildren();
    if (!items || !items.length) { list.appendChild(element('li', 'db-data-message', items ? uiText('暂无最新动态') : uiText('动态读取失败，请刷新重试'))); return; }
    items.slice(0, 5).forEach(function (item) {
      var row = element('li', 'db-news-item'), link = element('a', '', item.title);
      link.href = dashboard.dataset.base + (item.type === 'info' ? 'info/detail/' : 'res/') + encodeURIComponent(item.id);
      link.title = item.title; row.appendChild(link); list.appendChild(row);
    });
  }
  function showTooltip(event) {
    var bar = event.target.closest('.db-bar-item'); if (!bar) return;
    tooltip.textContent = uiText('{month} · {count} 次访问', {month:bar.dataset.month, count:number(bar.dataset.count)}); tooltip.style.display = 'block';
    var panel = bars.parentNode.getBoundingClientRect(), rect = bar.getBoundingClientRect();
    tooltip.style.left = Math.max(0, Math.min(rect.left - panel.left, panel.width - tooltip.offsetWidth)) + 'px'; tooltip.style.top = '0';
  }
  bars.addEventListener('pointerover', showTooltip); bars.addEventListener('focusin', showTooltip);
  bars.addEventListener('pointerout', function () { tooltip.style.display = 'none'; });
  bars.addEventListener('focusout', function () { tooltip.style.display = 'none'; });
  exportButton.addEventListener('click', function () {
    if (!data) return;
    var source = data.accessSource === 'site' ? uiText('站点访问量统计') : uiText('特色库资源访问量统计');
    var rows = [[uiText('月份'), uiText('访问量（次）'), uiText('统计来源')]];
    data.access.forEach(function (row) { rows.push([data.year + '-' + String(row.month).padStart(2, '0'), row.count, source]); });
    var csv = '\uFEFF' + rows.map(function (row) { return row.map(function (cell) { return '"' + String(cell).replace(/"/g, '""') + '"'; }).join(','); }).join('\r\n');
    var url = URL.createObjectURL(new Blob([csv], { type: 'text/csv;charset=utf-8' }));
    var link = element('a'); link.href = url; link.download = uiText('敦煌数据库月度访问量-') + data.year + '.csv'; link.click();
    setTimeout(function () { URL.revokeObjectURL(url); }, 1000);
  });
  function load() {
    if (loading) return;
    loading = true; dashboard.setAttribute('aria-busy', 'true'); refreshButton.disabled = true; refreshButton.classList.add('is-loading');
    updateState.textContent = uiText('数据更新中…'); updateState.classList.remove('is-error');
    var controller = new AbortController(); var timeout = setTimeout(function () { controller.abort(); }, 15000);
    return fetch(dashboard.dataset.base + 'database/statistics', { credentials: 'same-origin', cache: 'no-store', signal: controller.signal })
      .then(function (response) { if (!response.ok) throw new Error('Statistics failed'); return response.json(); })
      .then(function (result) {
        if (!result.success) throw new Error('Statistics failed');
        growthChart(result.growth); hotwords(result.hotwords); accessChart(result); latest(result.latest);
        data = result;
        [['db-resource-total', result.resourceTotal], ['db-page-total', result.pageTotal], ['db-recent-total', result.recentTotal], ['db-access-month', result.accessMonth]].forEach(function (item) {
          var node = document.getElementById(item[0]); node.textContent = number(item[1]);
          node.classList.toggle('is-long-number', String(node.textContent).length > 7);
        });
        change('db-resource-change', result.todayAdded, result.dailyResourceChangePercent, uiText('本'));
        change('db-recent-change', result.todayAdded, result.dailyResourceChangePercent, uiText('本'));
        change('db-access-change', result.todayAccess, result.dailyAccessChangePercent, uiText('次'));
        document.getElementById('db-page-change').textContent = result.pageTotal == null ? uiText('页码统计暂不可用') : uiText('页面索引实时统计');
        document.getElementById('db-statistics-status').textContent = uiText('数据已更新。文献按实际入库时间统计，页码为页面索引总数，访问数据为本月访问次数。');
        updateState.textContent = uiText('数据实时更新'); updateState.title = uiText('最近更新 {time}，每分钟自动刷新', {time:new Date().toLocaleTimeString('zh-CN', { hour12: false })}); exportButton.disabled = false;
      }).catch(function () {
        if (!data) {
          document.getElementById('db-trend').replaceChildren(element('p', 'db-data-message', uiText('统计读取失败，请刷新重试')));
          document.getElementById('db-hotwords').replaceChildren(element('p', 'db-data-message', uiText('热词读取失败，请刷新重试')));
          latest(null);
          ['db-resource-change', 'db-recent-change', 'db-access-change'].forEach(function (id) { document.getElementById(id).textContent = uiText('暂不可用'); });
        }
        updateState.textContent = data ? uiText('更新失败，显示上次数据') : uiText('更新失败，请刷新'); updateState.classList.add('is-error');
        document.getElementById('db-statistics-status').textContent = updateState.textContent;
      }).finally(function () {
        clearTimeout(timeout); loading = false; dashboard.setAttribute('aria-busy', 'false'); refreshButton.disabled = false; refreshButton.classList.remove('is-loading');
      });
  }
  function clock() {
    var now = new Date(), pad = function (n) { return String(n).padStart(2, '0'); };
    var date = now.getFullYear() + '-' + pad(now.getMonth() + 1) + '-' + pad(now.getDate());
    document.getElementById('db-time').textContent = pad(now.getHours()) + ':' + pad(now.getMinutes()) + ':' + pad(now.getSeconds());
    document.getElementById('db-time').dateTime = now.toISOString();
    document.getElementById('db-date').textContent = date; document.getElementById('db-date').dateTime = date;
    document.getElementById('db-weekday').textContent = [uiText('星期日'),uiText('星期一'),uiText('星期二'),uiText('星期三'),uiText('星期四'),uiText('星期五'),uiText('星期六')][now.getDay()];
  }
  refreshButton.addEventListener('click', load);
  clock(); setInterval(clock, 1000); load();
  setInterval(function () { if (!document.hidden) load(); }, 60000);
  document.addEventListener('visibilitychange', function () { if (!document.hidden) { clock(); load(); } });
})();
