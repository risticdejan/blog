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

        $.ajax({
            url: url,
            data: data,
            type: 'POST',
            dataType: 'JSON'
        }).done(function (data) {
            window.location = backUrl;
        }).fail(function (err) {
//            console.log(err);
        });


        e.preventDefault();
        e.stopPropagation();
    }
};

User.init();


