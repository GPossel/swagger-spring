var xhr;
window.addEventListener("load", function (name, value) {

    document.getElementById('btn_allTransactions').addEventListener('click',function (e) {
        xhr = new XMLHttpRequest();
        var url = 'https://devopsdeployment-1.herokuapp.com/transactions/search'
        console.log(url);

        var userPerformer = document.getElementById('userPerformer_search').value;
        var IBAN = document.getElementById('IBAN_search').value;
        var transferAmount = document.getElementById('transferAmount_search').value;
        var MaxNumberOfResults = document.getElementById('MaxNumberOfResults_search').value;
        url = url + '?userPerformer='+userPerformer+
            '&IBAN='+IBAN+
            '&transferAmount='+transferAmount+
            '&MaxNumberOfResults='+MaxNumberOfResults;

        console.log(url);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4){
                CreateTableFromJSON();
            }
        };
        xhr.open('GET', url);
        const session = sessionStorage.getItem("Authorization");
        xhr.setRequestHeader("Accept", "*/*");
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.setRequestHeader("Authorization", session);
        xhr.send();
    });

    document.getElementById('btn_sendTransaction').addEventListener('click', function (e) {
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'https://devopsdeployment-1.herokuapp.com/transactions');
        //Send the proper header information along with the request
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-type", "application/json");
        const session = sessionStorage.getItem("Authorization");
        xhr.setRequestHeader("Authorization", session)
        xhr.onload = function () {
            switch (xhr.status) {
                case 201:
                    alert(xhr.status + ":" + xhr.responseText);
                    break;
                case 400:
                    alert(xhr.status + ":" + xhr.responseText);
                    break;
                case 403:
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
        }
        xhr.send(JSON.stringify({
            "ibanSender": document.getElementById('ibanSender').value,
            "ibanReciever": document.getElementById('ibanReciever').value,
            "transferAmount": document.getElementById('transferAmount').value
        }, ));

    });

    document.getElementById('btn_transactionById').addEventListener('click',function (e) {
        xhr = new XMLHttpRequest();
        var url = 'https://devopsdeployment-1.herokuapp.com/transactions/';
        var transactionID = document.getElementById('transactionID_search').value;
        if(transactionID === ""){
            alert("Transaction ID field is empty");
            return;
        }
        url = url + transactionID;

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4){
                document.getElementById("showData-byId").innerHTML = "";
                CreateTableFromJSONbyID();
            }
        };
        xhr.open('GET', url);
        const session = sessionStorage.getItem("Authorization");
        xhr.setRequestHeader("Accept", "*/*");
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.setRequestHeader("Authorization", session);
        xhr.send();
    });

    document.getElementById('btn_transactionByIBAN').addEventListener('click',function (e) {
        xhr = new XMLHttpRequest();
        var url = 'https://devopsdeployment-1.herokuapp.com/transactions/';
        var transactionIBAN = document.getElementById('transactionIBAN_search').value;
        if(transactionIBAN === ""){
            alert("Transaction transactionIBAN field is empty");
            return;
        }
        url = url + transactionIBAN + '/accounts';

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4){
                document.getElementById("showData-byIBAN").innerHTML = "";
                CreateTableFromJSONByIBAN("showData-byIBAN");
            }
        };
        xhr.open('GET', url);
        const session = sessionStorage.getItem("Authorization");
        xhr.setRequestHeader("Accept", "*/*");
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.setRequestHeader("Authorization", session);
        xhr.send();
    });

});

function CreateTableFromJSON() {
    var myItems = JSON.parse(xhr.responseText);

    // EXTRACT VALUE FOR HTML HEADER.
    // ('Book ID', 'Book Name', 'Category' and 'Price')
    var col = [];
    for (var i = 0; i < myItems.length; i++) {
        for (var key in myItems[i]) {
            if (col.indexOf(key) === -1) {
                col.push(key);
            }
        }
    }
    // CREATE DYNAMIC TABLE.
    var table = document.createElement("table");

    // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.

    var tr = table.insertRow(-1);                   // TABLE ROW.

    for (var i = 0; i < col.length; i++) {
        var th = document.createElement("th");      // TABLE HEADER.
        th.innerHTML = col[i];
        tr.appendChild(th);
    }

    // ADD JSON DATA TO THE TABLE AS ROWS.
    for (var i = 0; i < myItems.length; i++) {

        tr = table.insertRow(-1);

        for (var j = 0; j < col.length; j++) {
            var tabCell = tr.insertCell(-1);
            tabCell.innerHTML = myItems[i][col[j]];
        }
    }

    // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
    var divContainer = document.getElementById('showData');
    divContainer.innerHTML = "";
    divContainer.appendChild(table);
}

function CreateTableFromJSONByIBAN(tablename) {
    var myItems = JSON.parse(xhr.responseText);

    // EXTRACT VALUE FOR HTML HEADER.
    // ('Book ID', 'Book Name', 'Category' and 'Price')
    var col = [];
    for (var i = 0; i < myItems.length; i++) {
        for (var key in myItems[i]) {
            if (col.indexOf(key) === -1) {
                col.push(key);
            }
        }
    }
    // CREATE DYNAMIC TABLE.
    var table = document.createElement("table");

    // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.

    var tr = table.insertRow(-1);                   // TABLE ROW.

    for (var i = 0; i < col.length; i++) {
        var th = document.createElement("th");      // TABLE HEADER.
        th.innerHTML = col[i];
        tr.appendChild(th);
    }

    // ADD JSON DATA TO THE TABLE AS ROWS.
    for (var i = 0; i < myItems.length; i++) {

        tr = table.insertRow(-1);

        for (var j = 0; j < col.length; j++) {
            var tabCell = tr.insertCell(-1);
            tabCell.innerHTML = myItems[i][col[j]];
        }
    }

    // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
    var divContainer = document.getElementById(tablename);
    divContainer.innerHTML = "";
    divContainer.appendChild(table);
}

function CreateTableFromJSONbyID() {
    var data = JSON.parse(xhr.responseText);
    // console.table(data);

    // (C) GENERATE TABLE
    // (C1) CREATE EMPTY TABLE
    var table = document.createElement("tableById"), row, cellA, cellB;

    document.getElementById("showData-byId").appendChild(table);
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






