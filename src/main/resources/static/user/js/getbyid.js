var xhr;
window.addEventListener("load", function (name, value) {

    document.getElementById('userId_search').addEventListener('click',function (e) {
        xhr = new XMLHttpRequest();
        var url = 'http://localhost:8080/users/'
        console.log(url);

        var userId = document.getElementById('userId').value;
        url = url + userId;

        console.log(url);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4){
                document.getElementById("showData").innerHTML = "";
                CreateTableFromJSON();
            }
        };
        xhr.open('GET', url);
        const session = sessionStorage.getItem("Authorization");
        xhr.setRequestHeader("Accept", "application/json");
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
    var table = document.createElement("tableById"), row, cellA, cellB;

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



