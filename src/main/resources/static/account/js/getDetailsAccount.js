var xhr;
window.addEventListener("load", function (name, value) {


document.getElementById('btn_accountByIBAN').addEventListener('click',function (e) {
    xhr = new XMLHttpRequest();
    var url = 'https://devopsdeployment-1.herokuapp.com/accounts/';
    var accountIBAN = document.getElementById('accountIBAN_search').value;
    if(accountIBAN === ""){
        alert("Account IBAN field is empty");
        return;
    }
    url = url + accountIBAN;

    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4){
            document.getElementById("showData").innerHTML = "";
            CreateTableFromJSON();
        }
    }
    xhr.open('GET', url);
    const session = sessionStorage.getItem("Authorization");
    xhr.setRequestHeader("Accept", "*/*");
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.setRequestHeader("Authorization", session);
    xhr.send();
    });

});

function CreateTableFromJSON() {
        var data = JSON.parse(xhr.responseText);
        // console.table(data);

        // (C) GENERATE TABLE
        // (C1) CREATE EMPTY TABLE
        var table = document.createElement("table"), row, cellA, cellB;

        document.getElementById("showData").appendChild(table);
        for (let key in data) {
            // (C2) ROWS & CELLS
            row = document.createElement("tr");
            cellA = document.createElement("td");
            cellB = document.createElement("td");

            // (C3) KEY & VALUE
            cellA.innerHTML = key;
            cellB.innerHTML = data[key];

            // (C4) ATTACH ROW & CELLS
            table.appendChild(row);
            row.appendChild(cellA);
            row.appendChild(cellB);
        }
}
