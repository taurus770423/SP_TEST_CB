class DataTable {
    constructor(params) {
        this.params = params;
        this.$target = this.jqueryParser(params.target);
        this.init();
        this.change(1);
    }

    jqueryParser = (target) => {
        return target instanceof jQuery ? target : $(target);
    }

    init() {
        let colCount = this.params.thead[0].length;

        let thead = `<thead>`;
        this.params.thead.forEach(h1 => {
            thead += `<tr>`
            h1.forEach(h2 => {
                thead += `<th class="${h2.class || ""}">${h2.text}</th>`
            })
            thead += `</tr>`
        })
        thead += `</thead>`;

        this.$target.replaceWith(`
            <table class="table_field" id=${this.params.id || ""}>
                ${thead}
                <tbody></tbody>
                <tfoot>
                    <tr class="pagination_row">
                        <td colspan="${colCount}">
                            <div id="pagination">
                                <div id="pagination_first_btn">«</i></div>
                                <div id="pagination_previous_btn">‹</div>
                                <div id="pagination_current_page"></div>／<div id="pagination_total_pages"></div>
                                <div id="pagination_next_btn">›</div>
                                <div id="pagination_last_btn">»</div>
                            </div>
                        </td>
                    </tr>
                    <tr class="empty_message_row">
                        <td colspan="${colCount}">查無資料</td>
                    </tr>
                </tfoot>
            </table>
        `)
        this.$target = this.jqueryParser("#" + this.params.id);

        this.$target.find("#pagination_first_btn").on("click", () => {
            if(this.pageNo > 1) this.change(1);
        });

        this.$target.find("#pagination_previous_btn").on("click", () => {
            if(this.pageNo > 1) this.change(this.pageNo - 1)
        });

        this.$target.find("#pagination_next_btn").on("click", () => {
            if(this.pageNo < this.totalPages) this.change(this.pageNo + 1)
        });

        this.$target.find("#pagination_last_btn").on("click", () => {
            if(this.pageNo < this.totalPages) this.change(this.totalPages);
        });
    }

    set(data, pageNo, totalPages) {
        let tbody = ``;
        for(let i=0; i<data.length; i++) {
            tbody += `<tr class="data_row">`;
            for(let j=0; j<data[i].length; j++) {
                let cls = (this.params.tbody && this.params.tbody[j]) ? this.params.tbody[j].class || "" : "";
                tbody += `<td class="${cls}">${data[i][j]}</td>`;
            }
            tbody += `</tr>`;
        }

        this.$target.find("tbody").empty().append(tbody);

        this.pageNo = pageNo;
        this.totalPages = totalPages;

        let $firstBtn = this.$target.find("#pagination_first_btn");
        let $previousBtn = this.$target.find("#pagination_previous_btn");
        if(pageNo > 1) {
            $firstBtn.removeClass("disable");
            $previousBtn.removeClass("disable");
        } else {
            $firstBtn.addClass("disable");
            $previousBtn.addClass("disable");
        }

        this.$target.find("#pagination_current_page").text(pageNo);
        this.$target.find("#pagination_total_pages").text(totalPages);

        let $nextBtn = this.$target.find("#pagination_next_btn");
        let $lastBtn = this.$target.find("#pagination_last_btn");
        if(pageNo < totalPages) {
            $nextBtn.removeClass("disable");
            $lastBtn.removeClass("disable");
        } else {
            $nextBtn.addClass("disable");
            $lastBtn.addClass("disable");
        }
    }

    change(pageNo) {
        this.pageNo = pageNo || this.pageNo;
        if(Number.isNaN(this.pageNo)) return;
        if(this.params.change) this.params.change(this.pageNo);
    }
}