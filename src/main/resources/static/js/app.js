const URL = "http://localhost:8080/stats-payment-by-date";

function ajaxExample() {
    fetch(URL)
        .then((res) => res.json())
        .then((data) => console.log(data));
}


/* AUTOCOMPLETE*/
const searchInputDep = document.getElementById('search-departure');
const searchWrapperDep = document.querySelector('.wrapper-departure');
const resultsWrapperDep = document.querySelector('.wrapper-departure .results');

const searchInputArr = document.getElementById('search-arriving');
const searchWrapperArr = document.querySelector('.wrapper-arriving');
const resultsWrapperArr = document.querySelector('.wrapper-arriving .results');

if (searchInputDep && searchWrapperDep && resultsWrapperDep && searchInputArr && searchWrapperArr && resultsWrapperArr) {
    searchInputDep.addEventListener('keyup', () => {
        showTowns(searchInputDep, searchWrapperDep, resultsWrapperDep);
    });
    searchInputArr.addEventListener('keyup', () => {
        showTowns(searchInputArr, searchWrapperArr, resultsWrapperArr);
    });
}

function showTowns(searchInput, searchWrapper, resultWrapper) {
    fetch(" http://localhost:8080/town-search?town-name=" + searchInput.value)
        .then((res) => res.json())
        .then((response) => {
            if (response) {
                let data = response.data;
                if (data && data.length > 0) {
                    data = data[0];
                    var result = [];
                    data.forEach((d) => {
                        result.push(d.name);
                    })
                    renderResults(result, searchWrapper, resultWrapper);
                    addLiListener(searchInput, searchWrapper, resultWrapper);
                }
            }
        });
}

function addLiListener(searchInput, searchWrapper, resultWrapper) {
    let allList = resultWrapper.querySelectorAll("li");
    for (let i = 0; i < allList.length; i++) {
        allList[i].addEventListener("click", function (element) {
            let selectData = this.textContent;
            searchInput.value = selectData;
            searchWrapper.classList.remove("show");
        });
    }
}

function renderResults(result, searchWrapper, resultWrapper) {
    if (!result.length) {
        return searchWrapper.classList.remove('show');
    }

    const content = result
        .map((item) => {
            return `<li>${item}</li>`;
        })
        .join('');

    searchWrapper.classList.add('show');
    resultWrapper.innerHTML = `<ul>${content}</ul>`;
}

/* END AUTOCOMPLETE */

$(".card-departure-flight").click(function (event) {
    let target = event.target;
    $(".card-departure-flight").css('background', '#00C4A7');
    let href = $("#btn-calculate-price").attr("href");
    let id_flight = target.dataset.idFlight;

    href = href.replace(/id-flight-dep=-?[0-9]+/gi, 'id-flight-dep=' + id_flight);

    $("#btn-calculate-price").attr("href", href);
    target.style.background = 'yellowgreen';

});

$(".card-arriving-flight").click(function (event) {
    let target = event.target;
    $(".card-arriving-flight").css('background', '#00C4A7');
    let href = $("#btn-calculate-price").attr("href");
    let id_flight = target.dataset.idFlight;
    href = href.replace(/id-flight-arr=-?[0-9]+/gi, 'id-flight-arr=' + id_flight);
    $("#btn-calculate-price").attr("href", href);
    target.style.background = 'yellowgreen';

});


$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });


    $.ajax({
        url: URL,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (response) {

            if (response) {
                if (response.data && response.data.length > 0) {

                    let data = response.data[0];
                    const labels = [];
                    const realData = [];
                    data.forEach((d) => {
                        labels.push(d.date);

                        realData.push({
                            x: Date.parse(d.date),
                            y: d.total_amount
                        });
                    });


                    var ctx = document.getElementById('myChart');
                    if (ctx) {
                        ctx = ctx.getContext('2d');
                        var myChart = new Chart(ctx, {
                            type: 'line',
                            data: {
                                labels: labels,
                                datasets: [{
                                    data: realData,
                                    label: "Total",
                                    borderColor: "#3e95cd",
                                    backgroundColor: "#7bb6dd",
                                    fill: false,
                                },
                                ]
                            },
                        });
                    }


                    var ctx = document.getElementById('myChart1');
                    if (ctx) {
                        ctx = ctx.getContext('2d');
                        var myChart = new Chart(ctx, {
                            type: 'line',
                            data: {
                                labels: labels,
                                datasets: [{
                                    data: realData,
                                    label: "Total",
                                    borderColor: "#3e95cd",
                                    backgroundColor: "#7bb6dd",
                                    fill: false,
                                },
                                ]
                            },
                        });
                    }

                }

            }

        },
    });

});
