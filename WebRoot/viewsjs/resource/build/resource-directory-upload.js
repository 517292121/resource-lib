(function ($) {
    'use strict';
    function mode() { return $('#directoryMode').val() || 'server'; }
    function paths() {
        var input = document.getElementById('directoryFiles'), names = [], total = 0;
        if (input && input.files) {
            for (var i = 0; i < input.files.length; i++) {
                names.push(input.files[i].webkitRelativePath || input.files[i].name);
                total += input.files[i].size;
            }
        }
        $('#directoryPaths').val(JSON.stringify(names));
        $('#directorySelection').text(names.length ? '已选择 ' + names.length + ' 个文件，共 ' + (total / 1024 / 1024).toFixed(2) + ' MB；保存时上传。' : '选择资源文件夹后，将保留目录结构上传到服务器。');
        return names;
    }
    window.resizeResourceUpload = function () {
        var index = parseInt($('#resFileType').val(), 10) || 0;
        var item = $('.tabbox .content > ul > li').eq(index);
        $('.tabbox .content').height(Math.max(150, item.outerHeight(true) || 150));
    };
    window.syncDirectoryUpload = function () {
        var active = $('#resFileType').val() === '1';
        var selected = mode();
        var current = parseInt($('#resFileType').val(), 10) || 0;
        $('.tabbox .content > ul > li').each(function (index) {
            $(this).find('input[type=file]').prop('disabled', index !== current);
        });
        $('[data-directory-mode]').each(function () {
            var enabled = $(this).attr('data-directory-mode') === selected;
            $(this).toggle(enabled).find('input').prop('disabled', !enabled || !active);
        });
        $('#directoryCover').prop('disabled', !active);
        $('#directoryUploadError').text('');
        window.resizeResourceUpload();
    };
    window.validateResourceDirectoryUpload = function () {
        if ($('#resFileType').val() !== '1') return true;
        var error = '', selected = mode();
        if (selected === 'server' && !$.trim($('#resourceDirectory').val())) error = '请输入服务器资源目录。';
        if (selected === 'zip' && !$('#resourceArchive').val()) error = '请选择 ZIP 压缩包。';
        if (selected === 'zip' && $('#resourceArchive').val() && !/\.zip$/i.test($('#resourceArchive').val())) error = '压缩包请使用 ZIP 格式。';
        if (selected === 'folder' && !paths().length) error = '请选择资源文件夹。';
        $('#directoryUploadError').text(error);
        if (error) { window.resizeResourceUpload(); document.getElementById('directoryUploadError').scrollIntoView({block:'nearest'}); }
        return !error;
    };
    $(function () {
        $('#directoryMode').on('change', window.syncDirectoryUpload);
        $('#directoryFiles').on('change', function () { paths(); window.resizeResourceUpload(); });
        window.syncDirectoryUpload();
    });
}(jQuery));
