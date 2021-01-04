var xhr;
window.addEventListener("load", function (name, value) {

    function RedirectPage(authorization) {
        xhr = new XMLHttpRequest();
        xhr.open('GET', 'https://devopsdeployment-1.herokuapp.com/transactions');
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.setRequestHeader("Authorization", authorization);
        xhr.send();
        window.location.replace("https://devopsdeployment-1.herokuapp.com/Transactions.html");
    }

    document.getElementById('button_login').addEventListener('click', function (e) {
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'https://devopsdeployment-1.herokuapp.com/login');
        //Send the proper header information along with the request
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.onload = function () {
            if (xhr.status == 200) {
                alert(xhr.status + ":" + xhr.responseText);
                let response = JSON.parse(xhr.response);
                console.log(response);
                let authorization = "Bearer " + response.jwt;
                sessionStorage.setItem("Authorization", authorization);
                RedirectPage(authorization);
            }
            else {
                alert(xhr.status + ":" + xhr.responseText);
            }
        }
        xhr.send(JSON.stringify({
            "username": document.getElementById('username').value,
            "password": document.getElementById('password').value,
        },));
    });

});