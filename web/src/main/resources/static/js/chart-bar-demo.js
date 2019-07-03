// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#ffffff';

// Bar Chart Example
var ctx = document.getElementById("myBarChart");
var myLineChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: [
            visitorPerDay[0].day.toLowerCase(),
            visitorPerDay[1].day.toLowerCase(),
            visitorPerDay[2].day.toLowerCase(),
            visitorPerDay[3].day.toLowerCase(),
            visitorPerDay[4].day.toLowerCase(),
            visitorPerDay[5].day.toLowerCase(),
            visitorPerDay[6].day.toLowerCase()],
        datasets: [{
                label: "Visitor",
                backgroundColor: "rgba(255,255,255,1)",
                borderColor: "rgba(0,0,0,1)",
                data: [
                    visitorPerDay[0].count,
                    visitorPerDay[1].count,
                    visitorPerDay[2].count,
                    visitorPerDay[3].count,
                    visitorPerDay[4].count,
                    visitorPerDay[5].count,
                    visitorPerDay[6].count
                ]
            }]
    },
    options: {
        scales: {
            xAxes: [{
                    time: {
                        unit: 'days'
                    },
                    gridLines: {
                        display: true
                    },
                    ticks: {
                        maxTicksLimit: 7
                    }
                }],
            yAxes: [{
                    ticks: {
                        min: 0,
                        max: max,
                        maxTicksLimit: 5
                    },
                    gridLines: {
                        display: true
                    }
                }]
        },
        legend: {
            display: false
        }
    }
});
