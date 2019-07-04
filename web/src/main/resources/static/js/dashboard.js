var Home = {
    config: {
        link: urlHome,
        numberOfUser: "#nou",
        numberOfArticles: "#noa",
        articlePerUser: "#apu",
        username: "#username",
        email: "#email",
        likes: "#likes",
        dislikes: "#dislikes",
        globarRate: "#gr"
    },

    init: function (config) {
        $.extend(this.config, config);

        this.bindEvents();
        this.get();
        this.refresh();
    },

    bindEvents: function () {
        var config = this.config;
    },

    refresh: function () {
        var self = this;
        setTimeout(function () {
            self.get().done(function (data) {
                self.update(data);
                self.refresh();
            }).fail(function (err) {
                self.refresh();
            });

        }, 2000);
    },

    get: function () {
        var self = this,
                config = self.config,
                link = config.link;

        return $.ajax({
            url: link,
            type: 'POST',
            dataType: 'JSON'
        });
    },

    update: function (data) {
        var self = this,
                config = self.config,
                $numberOfUser = $(config.numberOfUser),
                $numberOfArticles = $(config.numberOfArticles),
                $articlePerUser = $(config.articlePerUser),
                $username = $(config.username),
                $email = $(config.email),
                $likes = $(config.likes),
                $dislikes = $(config.dislikes),
                $globalRate = $(config.globarRate);

        if (data.status === "success") {
            self.setPie(data);
            self.setLine(data);

            $numberOfUser.html(data.body.userCount);
            $numberOfArticles.html(data.body.articleCount);
            $articlePerUser.html((data.body.articleCount / data.body.userCount).toFixed(2));
            $username.html(data.body.user.username);
            $email.html(data.body.user.email);
            $likes.html(data.body.likesCount);
            $dislikes.html(data.body.dislikesCount);
            $globalRate.html((data.body.likesCount / data.body.dislikesCount).toFixed(2));
        }
    },

    setPie: function (data) {
        configPie.data = {
            labels: ["Like", "Dislike"],
            datasets: [{
                    data: [data.body.likesCount, data.body.dislikesCount],
                    backgroundColor: ['#007bff', '#dc3545']
                }]
        };
        window.pie.update();
    },

    setLine: function (data) {
        var visitorPerDay = data.body.visitorPerDay;
        configLine.data = {
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
        };
        configLine.options.scales.yAxes[0].ticks.max = data.body.max;


        window.line.update();
    }
};

Home.init();

