
$(document).ready(function (){

    $('body').on('click', '#filters', function () {
        let url = "https://devopsdeployment-1.herokuapp.com/accounts";
        let rank = $("#rank").val();
        let status = $("#status").val();
        url += "?rank=" + rank.toLocaleUpperCase() + "&status=" + status.toUpperCase();

        loadUsers(url);
    });

    $('body').on('click', '#AccountsForUser', function () {
        let userId = $("#userId").val();
        if (userId.length > 0){
            loadUsers("https://devopsdeployment-1.herokuapp.com/accounts/" + userId + "/users");
        }
    });

    function loadUsers(url){

        let xhr = new XMLHttpRequest();
        xhr.open("GET", url);
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-type", "application/json");
        const session = sessionStorage.getItem("Authorization");
        xhr.setRequestHeader("Authorization", session)

        xhr.onload = (e) => {
            switch (xhr.status) {
                case 200:
                case 201:
                    $("#result thead tr  td").parent().remove();

                    let responsData = JSON.parse(xhr.responseText);
                    let table = document.getElementById('result');
                    let row, arrayData, editUrlStr, i, j;

                    //Iterate through every row off the response data
                    for (i = 0; i < responsData.length; i++) {
                        //Make associative array off the object with data
                        console.log(responsData[i]);
                        arrayData = Object.entries(responsData[i]);
                        row = table.insertRow(i + 1);
                        editUrlStr = "";
                        //Iterate through the fields of the response data && Add new Cell in table with data
                        for (j = 0; j < arrayData.length; j++) {
                            row.insertCell(j).innerHTML = arrayData[j][1];
                            //create urlParameters for the edit url
                            if (j !== 0) {
                                editUrlStr += "&";
                            }
                            editUrlStr += arrayData[j][0] + "=" + arrayData[j][1];

                        }
                        row.insertCell(j).innerHTML = "<a class='edit' href='UpdateAccounts.html?" + editUrlStr + "'>edit</a>";
                        row.insertCell(j + 1).innerHTML = "<a class='delete' rel='" + responsData[i].iban + "' href=''>delete</a>";
                    }
                    break;
                case 400:
                    alert(xhr.status + ":" + xhr.responseText);
                    break;
                case 403:
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
        }
        xhr.send();
    }

    $('body').on('click', '.delete', function(){
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function(result){
            switch (xhr.status) {
                case 200:
                    alert("Deleted Succesfully.");
                    loadUsers();
                    break;
                case 400:
                    alert(xhr.status + ":" + xhr.responseText);
                    break;
                case 403:
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
        };
        let iban = $(this).attr('rel');
        console.log(iban);
        xhr.open('DELETE', 'https://devopsdeployment-1.herokuapp.com/accounts/' + iban);
        const session = sessionStorage.getItem("Authorization");
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.setRequestHeader("Authorization", session);
        xhr.send();
    });

});



