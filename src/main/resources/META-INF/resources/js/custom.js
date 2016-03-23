$(document).ready(function () {
    $("#delete-all-data").click(function () {
        var address = $(this).data("address");
        if ($.get("/"+address+"?delete"))
        {
            window.open('/'+address, '_self');
        }
        else
        {
            alert("failed to delete all data for /"+address);
        }
    })

    $("#delete-one").click(function () {
        var address = $(this).data("address");
        var pk = $(this).data("pk");
        if ($.get("/?delete&pk="+pk))
        {
            window.open('/'+address, '_self');
        }
        else
        {
            alert("failed to delete all data for item "+pk);
        }

    })
});