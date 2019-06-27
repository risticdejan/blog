// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#ffffff';

// Bar Chart Example
var ctx = document.getElementById("myBarChart");
var myLineChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
        datasets: [{
                label: "Revenue",
                backgroundColor: "rgba(255,255,255,1)",
                borderColor: "rgba(0,0,0,1)",
                data: [4215, 5312, 6251, 7841, 9821, 14984, 6445]
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
                        max: 15000,
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
