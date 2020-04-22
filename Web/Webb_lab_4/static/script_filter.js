function filter_todo(status) {
    const current_select = document.getElementById('dropdown-button');
    console.log('pick');
    if (status === undefined || status === '')
        status = current_select.innerText;
    else
        current_select.innerText = status;

    const search_text = document.getElementById('search').value;
    const todos = document.getElementsByClassName('card');
    for (const todo of todos) {
        const todoTitle = todo.querySelector('h5').textContent;
        if (todoTitle.indexOf(search_text) !== -1) {
            const checkbox = todo.querySelector('input');
            switch (status) {
                case 'All':
                    todo.style.display = '';
                    break;
                case 'Done':
                    if (checkbox.checked)
                        todo.style.display = '';
                    else
                        todo.style.display = 'none';
                    break;
                case 'Not done':
                    if (checkbox.checked)
                        todo.style.display = 'none';
                    else
                        todo.style.display = '';
                    break;
            }
        } else {
            todo.style.display = 'none';
        }
    }
}