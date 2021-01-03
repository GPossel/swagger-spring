
$(document).ready(function (){

    $('body').on('click', '#filters', function(){
        let url = "http://localhost:8080/accounts";

        let rank = $("#rank").val();
        let status = $("#status").val();

        url += "&rank=" + rank.toLocaleUpperCase() + "&status=" + status.toUpperCase();

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
                        arrayData = Object.entries(responsData[i]);
                        row = table.insertRow(i + 1);
                        editUrlStr = "";
                        //Iterate through the fields of the response data && Add new Cell in table with data
                        for (j = 0; j < arrayData.length; j++) {

                            if (arrayData[j][0] !== "password") {
                                row.insertCell(j).innerHTML = arrayData[j][1];
                                //create urlParameters for the edit url
                                if (j !== 0) {
                                    editUrlStr += "&";
                                }
                                editUrlStr += arrayData[j][0] + "=" + arrayData[j][1];
                            } else {
                                row.insertCell(j).innerHTML = "****";
                            }
                        }

                        row.insertCell(j).innerHTML = "<a class='edit' href='UpdateAccounts.html?" + editUrlStr + "'>edit</a>";
                        row.insertCell(j + 1).innerHTML = "<a class='delete' rel='" + responsData[i].id + "' href=''>delete</a>";
                    }
                    alert("Successful added User.");
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

    });

    $('body').on('click', '.delete', function(){
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function(result){
            loadUsers();
            switch (xhr.status) {
                case 200:
                    alert("User delete.");
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
        let userId = $(this).attr('rel');
        xhr.open('DELETE', 'http://localhost:8080/accounts/' + userId);
        const session = sessionStorage.getItem("Authorization");
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.setRequestHeader("Authorization", session);
        xhr.send();
    });

    $('body').on('click', '.edit', function(){
        console.log(1);
        $("#result-success").append("<p>test0</p>");

        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function(result){
            if (xhr.readyState === 4){

                let responsData = JSON.parse(xhr.responseText);

                $("#result-success").append("<p>test</p>");

                $("#result-success").append('<form><input type="text" name="userId" id="userId" value="'+ responsData[i].id +'" hidden>' +
                    '<label for="firstname">Firstname: </label>' +
                    '<input type="text" name="firstname" id="firstname" value="'+ responsData[i].firstname +'">' +
                    '<label for="lastname">Lastname: </label>' +
                    '<input type="text" name="lastname" id="lastname" value="'+ responsData[i].lastname +'">' +
                    '<label for="email">Email: </label>' +
                    '<input type="email" name="email" id="email" value="'+ responsData[i].email +'">' +
                    '<label for="phone">Phone: </label>' +
                    '<input type="text" name="phone" id="phone" value="'+ responsData[i].phone +'">' +
                    '<label for="birthdate">Birthdate: </label>' +
                    '<input type="date" name="birthdate" id="birthdate" value="'+ responsData[i].birthdate +'">' +
                    '<label for="registrationdate">Registrationdate: </label>' +
                    '<input type="date" name="registrationdate" id="registrationdate" value="'+ responsData[i].registrationdate +'">' +
                    '<input type="submit" name="update" id="update" value="update">' +
                    '</form>');
            }
        };

        let userId = $(this).attr('rel');
        xhr.open('GET', 'http://localhost:8080/accounts/' + userId);
        const session = sessionStorage.getItem("Authorization");
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.setRequestHeader("Authorization", session);

        xhr.send();
    });
});



