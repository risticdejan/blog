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


