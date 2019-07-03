$(document).ready(function () {
    $('#myModal').modal('show');

    $('#myModal').on('shown.bs.modal', function (e) {
        setTimeout(function () {
            $('#myModal').modal('hide');
        }, 4000);
    });

    $("#menu-toggle").click(function (e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
});


var User = {
    config: {
        form: '#ban-user',
        back: '#back-button'
    },

    init: function (config) {
        $.extend(this.config, config);

        this.bindEvents();
    },

    bindEvents: function () {
        var config = this.config,
                form = config.form;

        $(form).find('button').on('click', $.proxy(this.send, this));
    },

    send: function (e) {
        var config = this.config,
                backUrl = $(config.back).attr("href"),
                $form = $(config.form),
                url = $form.attr('action'),
                data = $form.serialize();
        console.log(backUrl);

        $.ajax({
            url: url,
            data: data,
            type: 'POST',
            dataType: 'JSON'
        }).done(function (data) {
            console.log(data);
            window.location = backUrl;
        }).fail(function (err) {
            console.log(err)
        });


        e.preventDefault();
        e.stopPropagation();
    }
};

var Home = {
    config: {
        link: url,
        numberOfUser: "#nou",
        numberOfArticles: "#noa",
        articlePerUser: "#apu",
        username: "#username",
        email: "#email"
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
            console.log("test");
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
                $email = $(config.email);

        if (data.status === "success") {
            self.setPie(data);
            self.setLine(data);

            $numberOfUser.html(data.body.userCount);
            $numberOfArticles.html(data.body.articleCount);
            $articlePerUser.html(data.body.articleCount / data.body.userCount);
            $username.html(data.body.user.username);
            $email.html(data.body.user.email);

            console.log(data);
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

User.init();
Home.init();

