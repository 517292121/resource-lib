riss2.favorites = {
    addFavorite: function(url, userId, sourceId) {
        if(!userId) {
            alert(poput_please_log_in_first);
            return;
        } else {
            if(userId && sourceId) {
                var param = {};
                param["userId"] = userId;
                param["sourceId"] = sourceId;                
                riss2.ajax.request(url, "POST", param, function() {
                    alert(riss2.ajax.xmlHttp.responseText);
                });
            }
        }
    }
}