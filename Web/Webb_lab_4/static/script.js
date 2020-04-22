function delete_todo(id) {
    let xhttp = new XMLHttpRequest();
    const url = '/delete_todo/' + id + '/';
    const method = 'get';
    xhttp.open(method, url, false);
    xhttp.setRequestHeader('X-Requested-With', 'XMLHttpRequest');

    const data = null;
    xhttp.send(data);
    if (xhttp.status >= 400) {
        alert(xhttp.responseText);
    } else {
        let list = document.getElementById("todo-list");
        let elem = document.getElementById("todo_" + id);
        list.removeChild(elem);
    }
}


function change_check(id) {
    let xhttp = new XMLHttpRequest();
    const url = '/change_check/' + id + '/';
    const method = 'get';
    xhttp.open(method, url, false);
    xhttp.setRequestHeader('X-Requested-With', 'XMLHttpRequest');

    const data = null;
    xhttp.send(data);
    if (xhttp.status >= 400) {
        alert(xhttp.responseText);
    } else {
        const text = document.getElementById("todo_text_" + id);
        text.setAttribute('style', 'text-decoration: line-through; ');
        const slider = document.getElementById("check_" + id);
        slider.checked = true;
        slider.disabled = true;
    }
}