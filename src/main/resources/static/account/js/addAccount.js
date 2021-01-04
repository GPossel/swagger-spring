$(document).ready(function (){
    console.log(1.1);

    $('body').on('click', '#submit', function(){
        let rank = $("#rank").val();
        let userId = $("#userId").val();

        if (rank === "" || userId === "") {
            alert("A field is empty");
            return;
        }

        let xhr = new XMLHttpRequest();
        xhr.open("POST", "https://devopsdeployment-1.herokuapp.com/accounts");
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-type", "application/json");
        const session = sessionStorage.getItem("Authorization");
        xhr.setRequestHeader("Authorization", session)

        xhr.onload = (e) => {
           switch (xhr.status) {
               case 201:
                   console.log(4);
                   console.log(xhr);
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

       let data = JSON.stringify({
           "userId": userId,
           "rank": rank
        });
        xhr.send(data);
    });
});
