var xhr;
window.addEventListener("load", function (name, value) {

    document.getElementById('button_logout').addEventListener('click', function (e) {
        var xhr = new XMLHttpRequest();
        xhr.open('DELETE', 'https://devopsdeployment-1.herokuapp.com/logout');
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.setRequestHeader("Authorization", sessionStorage.getItem("Authorization"));
        xhr.onload = (e) => {
            sessionStorage.setItem("Authorization", "");
            window.location.replace("https://devopsdeployment-1.herokuapp.com/Login.html");
        }
        xhr.send();
    });
});