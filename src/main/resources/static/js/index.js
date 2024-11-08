let datatable
$(document).ready(() => {
    datatable= new DataTable({
        target: "#currency_datatable",
        id: "currency_list",
        thead: [
            [
                {text: `<input name="code" value="">`, class: "width_25 bg_white"},
                {text: `<input name="label" value="">`, class: "width_25 bg_white"},
                {text: "---", class: "width_25 bg_white"},
                {text: "---", class: "width_25 bg_white"},
                {text: `<i id="add_currency" class="fa-solid fa-plus"></i>`, class: "bg_white"}
            ], [
                {text: "幣別", class: ""},
                {text: "名稱", class: ""},
                {text: "建立時間", class: ""},
                {text: "修改時間", class: ""},
                {text: "", class: ""}
            ]
        ], tbody: [
            {class: "text_center"},
            {class: "text_center"},
            {class: "text_center"},
            {class: "text_center"},
            {class: "text_center"}
        ], change: (pageNo) => {
            $.ajax({
                url: "/currency/list",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                    code: $("#currency_list thead input[name='code']").val(),
                    label: $("#currency_list thead input[name='label']").val(),
                    pageNo: pageNo,
                    pageSize: 5
                }), success: (response) => {
                    let data = [];

                    if(response.data.currencyList) {
                        response.data.currencyList.forEach(currency => {
                            data.push([
                                `<input name="code" value="${currency.code}">`,
                                `<input name="label" value="${currency.label}">`,
                                currency.createTime,
                                currency.modifyTime,
                                `<i name="delete_currency" class="fa-solid fa-minus"></i>`
                            ])
                        })
                    }
                    datatable.set(data, pageNo, response.data.totalPages);
                }
            });
        }
    });

    let $currencyTableHead = $("#currency_list thead");
    $currencyTableHead.find("input[name='code'], input[name='label']").on("input", () => {
        datatable.change(1)
    });

    $currencyTableHead.on("click", "#add_currency", () => {
        let $thead = $("#currency_list thead");
        let $code = $thead.find("input[name='code']");
        let $label = $thead.find("input[name='label']");

        $.ajax({
            url: "/currency/exists",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({code: $code.val()}),
            success: (response) => {
                switch(response.status) {
                    case "SUCCESS":
                        if(response.data) {
                            Swal.fire({
                                icon: "warning",
                                text: "已存在相同的幣別",
                                confirmButtonText: "確認"
                            });
                        } else {
                            $.ajax({
                                url: "/currency/save",
                                method: "POST",
                                contentType: "application/json",
                                data: JSON.stringify({code: $code.val(), label: $label.val()}),
                                success: (response) => {
                                    switch(response.status) {
                                        case "SUCCESS":
                                            Swal.fire({
                                                icon: "info",
                                                text: "新增完成",
                                                confirmButtonText: "確認"
                                            });
                                            $code.val("");
                                            $label.val("");
                                            datatable.change(1);
                                            break;
                                        case "FAIL":
                                            Swal.fire({
                                                icon: "error",
                                                text: "新增失敗",
                                                confirmButtonText: "確認"
                                            });
                                            break;
                                    }
                                }
                            })
                        }
                        break;
                    case "FAIL":
                        Swal.fire({
                            icon: "error",
                            text: "系統異常",
                            confirmButtonText: "確認"
                        });
                        break;
                }
            }
        })
    });

    let $currencyTableBody = $("#currency_list tbody");
    $currencyTableBody.on("click", "svg[name='delete_currency']", (event) => {
        let $target = $(event.currentTarget);
        let code = $target.closest("tr").find("input[name='code']").val();

        Swal.fire({
            icon: "warning",
            text: `確定要刪除 ${code}？`,
            showCancelButton: true,
            confirmButtonText: "確認",
            cancelButtonText: "取消"
        }).then((result) => {
            if(result.isConfirmed) {
                $.ajax({
                    url: "/currency/delete",
                    method: "POST",
                    contentType: "application/json",
                    data: JSON.stringify({code: code}),
                    success: (response) => {
                        switch(response.status) {
                            case "SUCCESS":
                                Swal.fire({
                                    icon: "info",
                                    text: "刪除成功",
                                    confirmButtonText: "確認"
                                });
                                datatable.change(1);
                                break;
                            case "FAIL":
                                Swal.fire({
                                    icon: "info",
                                    text: "刪除失數",
                                    confirmButtonText: "確認"
                                });
                                break;
                        }
                    }
                })
            }
        });
    })

    setInterval(() => {
        getCoinDetails();
    }, 30000);
    getCoinDetails();
})

const getCoinDetails = () => {
    $.ajax({
        url: "/coin/details",
        method: "GET",
        contentType: "application/json",
        success: (response) => {
            if(response.status === "SUCCESS") {
                let data = response.data;

                $("#coin_chart_name").text(data.chartName);
                $("#coin_utc").text(data.updateTime);
                $("#coin_tw").text(data.updateTimeTW);

                let $tbody = $("#coin_table tbody");
                $tbody.empty();
                data.detailsList.forEach((details, index) => {
                    $tbody.append(`<tr>
                        <td>${details.code}</td>
                        <td>${details.label || "---"}</td>
                        <td>${details.rate}</td>
                    </tr>`);
                })
            }
        }
    })
}