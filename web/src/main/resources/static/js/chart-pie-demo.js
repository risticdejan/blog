Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#fff';

var ctx2 = document.getElementById("pieChartLikeRate");
var configPie = {
    type: 'pie',
    data: {
        labels: ["Like", "Dislike"],
        datasets: [{
                data: [likesCount, dislikesCount],
                backgroundColor: ['#007bff', '#dc3545']
            }]
    },
    options: {
        animation: false
    }
};

window.pie = new Chart(ctx2, configPie);
