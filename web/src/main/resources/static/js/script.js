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

$.validator.addMethod('filesize', function (value, element, param) {
    return this.optional(element) || (element.files[0].size <= param);
}, "The file can not be larger than 1 MB");

$.validator.addMethod("nameRules", function (value, element) {
    return this.optional(element) || /^[A-Za-z0-9._\\-]+$/i.test(value);
}, "The field can contain characters, digits, underscore and/or dash");

$.validator.addMethod("phoneRules", function (value, element) {
    return this.optional(element) || /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/im.test(value);
}, "Please specify a valid phone number");

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

    if (nameSplit.length === 1) {
        initials = nameSplit[0] ? nameSplit[0].charAt(0) : '?';
    } else {
        initials = nameSplit[0].charAt(0) + nameSplit[1].charAt(0);
    }

    if (window.devicePixelRatio) {
        size = (size * window.devicePixelRatio);
    }

    charIndex = (initials === '?' ? 72 : initials.charCodeAt(0)) - 64;
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

var Article = {
    config: {
        spinner: '.loading',
        form: '#article-form',
        body: '#body',
        title: '#title',
        description: '#description',
        category: '#categoryId',
        formLike: '.form-like',
        formDislike: '.form-dislike',
        labelLikes: '.label-likes',
        labelDislikes: '.label-dislikes',
        navbarDropdown: "#navbarDropdown"
    },

    init: function (config) {
        $.extend(this.config, config);

        $(this.config.spinner).hide();

        this.bindEvents();
    },

    bindEvents: function () {
        var config = this.config,
                form = config.form,
                formLike = config.formLike,
                formDislike = config.formDislike;

        $(form).find('button').on('click', $.proxy(this.save, this));
        $(form).on('focus', 'textarea', this.removeError);
        $(form).on('focus', 'input', this.removeError);

        $(formLike).find('button').on('click', this.like);
        $(formDislike).find('button').on('click', this.dislike);
    },

    save: function (e) {
        var config = this.config,
                $form = $(config.form),
                url = $form.attr('action');

        validator = this.validateForm(config.form);

        if (validator.form()) {
            $(config.spinner).show();

            var formData = new FormData($form[0]);

            $.ajax({
                url: url,
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                type: 'POST',
                dataType: 'JSON'
            }).done(function (data) {
                $(config.spinner).hide();

                if (data.status === "success") {
                    window.location = data.body.url;
                } else if (data.status === "failed") {
                    if (data.body.body) {
                        $(config.body).closest('div')
                                .append(Template.errorField(data.body.body));
                    }
                    if (data.body.title) {
                        $(config.title).closest('div')
                                .append(Template.errorField(data.body.title));
                    }
                    if (data.body.description) {
                        $(config.description).closest('div')
                                .append(Template.errorField(data.body.description));
                    }
                    if (data.body.categoryId) {
                        $(config.category).closest('div')
                                .append(Template.errorField(data.body.categoryId));
                    }
                }
            }).fail(function (e) {
                $(config.spinner).hide();
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
                    minlength: 4
                },
                title: {
                    required: true,
                    noSpecialCharacters: true,
                    minlength: 4,
                    maxlength: 155
                },
                description: {
                    required: true,
                    noSpecialCharacters: true,
                    minlength: 4,
                    maxlength: 255
                },
                categoryId: {
                    required: true
                },
                image: {
                    required: true,
                    extension: "jpg|jpeg|png",
                    filesize: 1000000
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
    },

    like: function (e) {
        var config = Article.config,
                $form = $(this).parents("form").eq(0),
                url = $form.attr('action'),
                data = $form.serialize(),
                $parent = $(e.currentTarget).parents("div").eq(0);
        // checking for if a user logged
        if ($(config.navbarDropdown).length > 0) {
            $.ajax({
                url: url,
                data: data,
                type: 'POST',
                dataType: 'JSON'
            }).done(function (data) {
                if (data.status === "success") {
                    $parent.find(config.labelLikes).html(data.body.likesCount);
                    $parent.find(config.labelDislikes).html(data.body.dislikesCount);
                }
                console.log(data);
            });
        }
        e.preventDefault();
        e.stopPropagation();
    },

    dislike: function (e) {
        var config = Article.config,
                $form = $(this).parents("form").eq(0),
                url = $form.attr('action'),
                data = $form.serialize(),
                $parent = $(e.currentTarget).parents("div").eq(0);
        // checking for if a user logged
        if ($(config.navbarDropdown).length > 0) {
            $.ajax({
                url: url,
                data: data,
                type: 'POST',
                dataType: 'JSON'
            }).done(function (data) {
                if (data.status === "success") {
                    $parent.find(config.labelLikes).html(data.body.likesCount);
                    $parent.find(config.labelDislikes).html(data.body.dislikesCount);
                }
            });
        }
        e.preventDefault();
        e.stopPropagation();
    }
};

var Comment = {
    config: {
        form: '#comment-add',
        list: '#comment-list',
        listItem: '.comment-item',
        body: "#body"
    },

    init: function (config) {
        $.extend(this.config, config);

        $(this.config.spinner).hide();

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
            $(config.spinner).hide();
            $.ajax({
                url: url,
                data: data,
                type: 'POST',
                dataType: 'JSON'
            }).done(function (data) {
                $(config.spinner).hide();
                if (data.status === "success") {
                    $list.prepend(Template.comment(data.body));
                } else if (data.status === "failed") {
                    $(config.body).closest('div')
                            .append(Template.errorField(data.body.body));
                }
            }).fail(function (e) {
                $(config.spinner).hide();
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

var Contact = {
    config: {
        spinner: '.loading',
        form: '#contact-form',
        name: '#name',
        email: '#email',
        phone: '#phone',
        message: '#message'
    },

    init: function (config) {
        $.extend(this.config, config);

        $(this.config.spinner).hide();

        this.bindEvents();
    },

    bindEvents: function () {
        var config = this.config,
                form = config.form;

        $(form).find('button').on('click', $.proxy(this.send, this));
        $(form).on('focus', 'textarea', this.removeError);
        $(form).on('focus', 'input', this.removeError);
    },

    send: function (e) {
        var config = this.config,
                $form = $(config.form),
                url = $form.attr('action'),
                data = $form.serialize();

        validator = this.validateForm(config.form);

        if (validator.form()) {
            $(config.spinner).show();

            $.ajax({
                url: url,
                data: data,
                type: 'POST',
                dataType: 'JSON'
            }).done(function (data) {
                $(config.spinner).hide();

                if (data.status === "success") {
                    window.location = data.body.url;
                } else if (data.status === "failed") {
                    if (data.body.name) {
                        $(config.name).closest('div')
                                .append(Template.errorField(data.body.name));
                    }
                    if (data.body.email) {
                        $(config.email).closest('div')
                                .append(Template.errorField(data.body.email));
                    }
                    if (data.body.phone) {
                        $(config.phone).closest('div')
                                .append(Template.errorField(data.body.phone));
                    }
                    if (data.body.body) {
                        $(config.message).closest('div')
                                .append(Template.errorField(data.body.body));
                    }
                }
            }).fail(function (e) {
                $(config.spinner).hide();
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
                },
                name: {
                    required: true,
                    nameRules: true,
                    minlength: 4,
                    maxlength: 511
                },
                email: {
                    required: true,
                    email: true,
                    minlength: 4,
                    maxlength: 511
                },
                phone: {
                    required: true,
                    phoneRules: true
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

var Auth = {
    config: {
        spinner: '.loading',
        formRegister: '#register-form',
        formLogin: '#login-form',
        username: '#username',
        email: '#email',
        password: '#password'
    },

    init: function (config) {
        $.extend(this.config, config);

        $(this.config.spinner).hide();

        this.bindEvents();
    },

    bindEvents: function () {
        var config = this.config;

        $(config.formRegister).find('button').on('click', $.proxy(this.register, this));
        $(config.formRegister).on('focus', 'input', this.removeError);
        $(config.formLogin).find('button').on('click', $.proxy(this.login, this));
        $(config.formLogin).on('focus', 'input', this.removeError);
    },

    register: function (e) {
        var config = this.config,
                $form = $(config.formRegister),
                url = $form.attr('action'),
                data = $form.serialize();

        validator = this.validateRegistrationForm(config.formRegister);

        if (validator.form()) {
            $(config.spinner).show();

            $.ajax({
                url: url,
                data: data,
                type: 'POST',
                dataType: 'JSON'
            }).done(function (data) {
                $(config.spinner).hide();

                if (data.status === "success") {
                    window.location = data.body.url;
                } else if (data.status === "failed") {
                    if (data.body.username) {
                        $(config.username).closest('div')
                                .append(Template.errorField(data.body.username));
                    }
                    if (data.body.email) {
                        $(config.email).closest('div')
                                .append(Template.errorField(data.body.email));
                    }
                    if (data.body.password) {
                        $(config.password).closest('div')
                                .append(Template.errorField(data.body.password));
                    }
                }
            }).fail(function (e) {
                $(config.spinner).hide();
            });
        }

        e.preventDefault();
        e.stopPropagation();
    },

    login: function (e) {
        var config = this.config,
                $form = $(config.formLogin),
                url = $form.attr('action'),
                data = $form.serialize();

        validator = this.validateLoginForm(config.formLogin);

        if (validator.form()) {
            $(config.spinner).show();
            $.ajax({
                url: url,
                data: data,
                type: 'POST',
                dataType: 'JSON'
            }).done(function (data) {
                $(config.spinner).hide();

                if (data.status === "success") {
                    window.location = data.body.url;
                } else if (data.status === "failed") {
                    if (data.body.username || data.body.password) {
                        $(config.username).closest('div')
                                .append(Template.errorField("Incorrect username or password"));
                    }
                }
            }).fail(function (e) {
                $(config.spinner).hide();
            });
        }

        e.preventDefault();
        e.stopPropagation();
    },

    removeError: function () {
        var $this = $(this);

        $('div.invalid-feedback').remove();
    },

    validateLoginForm: function (form) {
        return $(form).validate({
            lang: 'en',
            rules: {
                username: {
                    required: true,
                    nameRules: true,
                    minlength: 4,
                    maxlength: 511
                },
                password: {
                    required: true
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
    },

    validateRegistrationForm: function (form) {
        return $(form).validate({
            lang: 'en',
            rules: {
                username: {
                    required: true,
                    nameRules: true,
                    minlength: 4,
                    maxlength: 511
                },
                email: {
                    required: true,
                    email: true,
                    minlength: 4,
                    maxlength: 511
                },
                password: {
                    required: true
                },
                confirmPassword: {
                    required: true,
                    equalTo: "#password"
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

Auth.init();
Contact.init();
Comment.init();
Article.init();
