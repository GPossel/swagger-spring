window.addEventListener("load", function (name, value) {

    document.getElementById('submit').addEventListener('click', function (e) {

        let iban = $("#IBAN").val();
        let pincode = $("#pincode").val();
        let transferAmount = $("#transferAmount").val();
        let request = $("#request").val();

        if (iban === "" || pincode === "" || transferAmount === "" || request === "") {
            alert("A field is empty");
            return;
        }

        let xhr = new XMLHttpRequest();
        let url = "http://localhost:8080/accounts/" + request.valueOf();
        xhr.open("PUT", url);
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-type", "application/json");
        let authorization = sessionStorage.getItem("Authorization");
        xhr.setRequestHeader("Authorization", authorization);

        xhr.onload = (e) => {
            switch (xhr.status) {
                case 200:
                    alert(xhr.status + ":" + xhr.responseText);
                    break;
                case 201:
                    alert(xhr.status + ":" + xhr.responseText);
                    break;
                case 400:
                    alert(xhr.status + ":" + xhr.responseText);
                    break;
                case 405:
                    alert(xhr.status + ":" + xhr.responseText);
                    break;
                case 422:
                    alert(xhr.status + ":" + xhr.responseText);
                    break;
                case 500:
                    alert(xhr.status + ":" + xhr.responseText);
                    break;
                default:
                    alert(xhr.status + ":" + xhr.responseText);
                    break;
            }
            console.log(xhr.responseText);
        };

        let data = JSON.stringify({
            "IBAN": iban,
            "pincode": pincode,
            "transferAmount": transferAmount
        });

        xhr.send(data);
    })
});