$(document).ready(function () {
    $('#myModal').modal('show');

    $('#myModal').on('shown.bs.modal', function (e) {
        setTimeout(function () {
            $('#myModal').modal('hide');
        }, 4000);
    });
});

$.validator.addMethod("noSpecialCharacters", function (value, element) {
    return this.optional(element) || /^[a-z0-9\s.,_\\-\\'\"!?]+$/i.test(value);
}, "The field cannot contain special characters");


var getImg = function (name, size) {

    name = name || '';
    size = size || 60;

    var colours = [
        "#1abc9c", "#2ecc71", "#3498db", "#9b59b6", "#34495e", "#16a085", "#27ae60", "#2980b9",
        "#8e44ad", "#2c3e50",
        "#f1c40f", "#e67e22", "#e74c3c", "#ecf0f1", "#95a5a6", "#f39c12", "#d35400", "#c0392b",
        "#bdc3c7", "#7f8c8d"
    ],
            nameSplit = String(name)
            .toUpperCase()
            .split(' '),
            initials, charIndex, colourIndex, canvas, context, dataURI;

    if (nameSplit.length == 1) {
        initials = nameSplit[0] ? nameSplit[0].charAt(0) : '?';
    } else {
        initials = nameSplit[0].charAt(0) + nameSplit[1].charAt(0);
    }

    if (window.devicePixelRatio) {
        size = (size * window.devicePixelRatio);
    }

    charIndex = (initials == '?' ? 72 : initials.charCodeAt(0)) - 64;
    colourIndex = charIndex % 20;
    canvas = document.createElement('canvas');
    canvas.width = size;
    canvas.height = size;
    context = canvas.getContext("2d");

    context.fillStyle = colours[colourIndex - 1];
    context.fillRect(0, 0, canvas.width, canvas.height);
    context.font = Math.round(canvas.width / 2) + "px Arial";
    context.textAlign = "center";
    context.fillStyle = "#FFF";
    context.fillText(initials, size / 2, size / 1.5);

    dataURI = canvas.toDataURL();
    canvas = null;

    return dataURI;
};

var date_format = function (d) {
    var month = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    var date = month[d.getMonth()] + " " + d.getDate() + ", " + d.getFullYear();
    var time = d.getHours() + ":" + d.getMinutes();
    return (date + " " + time);
};



var Template = {
    comment: function (data) {
        var imgUri = getImg(data.user.username, 30);
        var date = date_format(new Date(data.createdAt));
        var temp = "";
        temp += "<li class='media comment-item'>";
        temp += "<img src='" + imgUri + "' alt=''>";
        temp += "<div class='media-body'>";
        temp += "<div>";
        temp += "<strong class='text-success'>@" + data.user.username + " </strong>";
        temp += "<small class='text-muted'> " + date + "</small>";
        temp += "</div>";
        temp += "<p>" + data.body + "</p>";
        temp += "</div>";
        temp += "</li>";

        return temp;
    },
    errorField: function (error) {
        var temp = "";
        temp += "<div class='invalid-feedback' style='display: block;'>";
        temp += error;
        temp += "</div>";
        return temp;
    }
};
//var Article = {
//    config: {
//        form: '#article-form'
//    },
//
//    init: function (config) {
//        $.extend(this.config, config);
//
//        this.bindEvents();
//    },
//
//    bindEvents: function () {
//        var config = this.config,
//                form = config.form;
//
//        $(form).find('button').on('click', $.proxy(this.addArticle, this));
//    },
//
//    addArticle: function (e) {
//        var config = this.config,
//                $form = $(config.form),
//                url = $form.attr('action'),
//                data = $form.serialize();
//
//        console.log("test");
//
//        validator = this.validateForm(config.form);
//
//        if (validator.form()) {
//            $.ajax({
//                url: url,
//                data: data,
//                type: 'POST',
//                dataType: 'JSON'
//            }).done(function (data) {
//                if (data.status === "success") {
//                    console.log(data);
//                }
//            });
//        }
//
//        e.preventDefault();
//        e.stopPropagation();
//    },
//
//    validateForm: function (form) {
//        return $(form).validate({
//            lang: 'en',
//            rules: {
//                body: {
//                    required: true,
//                    noSpecialCharacters: true,
//                    minlength: 4,
//                    maxlength: 511
//                }
//            },
//            highlight: function (element) {
//                $(element).closest('.form-group').addClass('has-error');
//            },
//            unhighlight: function (element) {
//                $(element).closest('.form-group').removeClass('has-error');
//            },
//            errorElement: 'div',
//            errorClass: 'invalid-feedback',
//            errorPlacement: function (error, element) {
//                if (element.parent('.input-group').length) {
//                    error.insertAfter(element.parent());
//                } else {
//                    error.insertAfter(element);
//                }
//            }
//        });
//    }
//};

var Comment = {
    config: {
        form: '#comment-add',
        list: '#comment-list',
        listItem: '.comment-item'
    },

    init: function (config) {
        $.extend(this.config, config);

        this.setImages();

        this.bindEvents();
    },

    bindEvents: function () {
        var config = this.config,
                form = config.form;

        $(form).find('button').on('click', $.proxy(this.addComment, this));
        $(form).on('focus', 'textarea', this.removeError);
    },

    setImages: function () {
        var config = this.config,
                $commentItem = $(config.listItem);

        $commentItem.each(function (index) {
            var $el = $(this),
                    img = $el.find("img"),
                    name = $el.find("strong").text().slice(1);

            img.attr("src", getImg(name, "30"));
        });
    },

    addComment: function (e) {
        var config = this.config,
                $list = $(config.list),
                $form = $(config.form),
                url = $form.attr('action'),
                data = $form.serialize();

        validator = this.validateForm(config.form);

        if (validator.form()) {
            $.ajax({
                url: url,
                data: data,
                type: 'POST',
                dataType: 'JSON'
            }).done(function (data) {
                if (data.status === "success") {
                    $list.prepend(Template.comment(data.body));
                } else if (data.status === "failed") {
                    $("#body")
                            .closest('div')
                            .append(
                                    Template.errorField(data.body.body)
                                    );
                }
            });
        }

        e.preventDefault();
        e.stopPropagation();
    },

    removeError: function () {
        var $this = $(this);

        $('div.invalid-feedback').remove();
    },

    validateForm: function (form) {
        return $(form).validate({
            lang: 'en',
            rules: {
                body: {
                    required: true,
                    noSpecialCharacters: true,
                    minlength: 4,
                    maxlength: 511
                }
            },
            highlight: function (element) {
                $(element).closest('.form-group').addClass('has-error');
            },
            unhighlight: function (element) {
                $(element).closest('.form-group').removeClass('has-error');
            },
            errorElement: 'div',
            errorClass: 'invalid-feedback',
            errorPlacement: function (error, element) {
                if (element.parent('.input-group').length) {
                    error.insertAfter(element.parent());
                } else {
                    error.insertAfter(element);
                }
            }
        });
    }
};

Comment.init();
//Article.init();
