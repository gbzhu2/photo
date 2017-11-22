/* *
 * 文件名: datepick.js
 * 描述: 日期插件
 *
 * 功能说明：展示选择日历
 *  
 * 版本: 
 * 作者: llyuan@iflytek.com
 * 日期： 
 * */

var DatePicker = (function () {
    var DatePicker = function () {
    };

    DatePicker.prototype.initDate = function (parameter) {
        var maxData = "2099-12-31 23:59:59";
        var minData = "1900-01-01 00:00:00";
        var showChearButton = false;
        var dataFormat = 'yyyy-MM-dd';
        var isShowToday = true;

        // 禁止退格键
        $(document).keydown(function (e) {
            var doPrevent;
            if (e.keyCode == 8) {
                var d = e.srcElement || e.target;
                if (d.tagName.toUpperCase() == 'INPUT'
                    || d.tagName.toUpperCase() == 'TEXTAREA') {
                    doPrevent = d.readOnly || d.disabled;
                } else
                    doPrevent = true;
            } else
                doPrevent = false;

            if (doPrevent)
                e.preventDefault();
        });

        if (parameter.dateFmt) {
            dataFormat = 'yyyy-MM-dd';
        }
        if (parameter.MaxDataRelyId != null) {
            maxData = "#F{$dp.$D(\'" + parameter.MaxDataRelyId + "\')}";
        }
        if (parameter.MinDataRelyId != null) {
            minData = "#F{$dp.$D(\'" + parameter.MinDataRelyId + "\')}";
        }
        if (parameter.MaxData != null) {
            maxData = parameter.MaxData;
        }
        if (parameter.MinData != null) {
            minData = parameter.MinData;
        }
        if (parameter.MaxIsTaday) {
            maxData = "%y-%M-%d";
        }
        if (parameter.MinIsTaday) {
            minData = "%y-%M-%d";
        }
        if (parameter.DefaultShow != null) {
            $("#" + parameter.InputId).val(parameter.DefaultShow);
        }
        if (!parameter.IsShowClear) {
            showChearButton = parameter.IsShowClear;
        }
        if (!parameter.isShowToday) {
            isShowToday = false;
        }

        $("#" + parameter.InputId).focus(function () {
            WdatePicker({
                maxDate: maxData,
                minDate: minData,
                skin: parameter.Skin,
                readOnly: true,
                isShowClear: showChearButton,
                DefaultShow: parameter.DefaultShow,
                onpicked: parameter.Callback,
                isShowToday: isShowToday,
                dateFmt: dataFormat
            })
        });
    };
    DatePicker.prototype.GetDateStr = function (AddDayCount) {
        var dd = new Date();
        dd.setDate(dd.getDate() + AddDayCount);// 获取AddDayCount天后的日期
        var y = dd.getFullYear();
        var m = (dd.getMonth()) >= 9 ? dd.getMonth() + 1 : "0"
            + (dd.getMonth() + 1);// 获取当前月份的日期
        var d = (dd.getDate()) > 9 ? dd.getDate() : "0" + dd.getDate();
        return y + "-" + m + "-" + d;
    };
    return DatePicker;
})();