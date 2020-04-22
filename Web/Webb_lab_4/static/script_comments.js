// json-server --watch db.json


function create_comment_element(json_comment) {
    const card = document.createElement('div');
    card.classList.add('card');

    const body = document.createElement('div');
    body.classList.add('card-body');

    const title = document.createElement('h5');
    title.classList.add('card-title');
    title.innerHTML = json_comment.name;

    const email = document.createElement('h6');
    email.classList.add('card-subtitle');
    email.classList.add('mb-2');
    email.innerHTML = json_comment.email;

    const text = document.createElement('p');
    text.classList.add('card-text');
    text.innerHTML = json_comment.body;

    body.appendChild(title);
    body.appendChild(email);
    body.appendChild(text);
    card.appendChild(body);

    return card;
}

async function create_comments() {
    const todo_id = Number(document.getElementsByClassName('card')[0].id);
    const comment_list = document.getElementById('comments_list');
    const url = 'http://localhost:3000/comments?postId=' + todo_id%100;

    const commentsResponse = await fetch(url);
    if (commentsResponse.status >= 400) {
        const error = document.createElement('div');
        error.classList.add('alert');
        error.classList.add('alert-danger');
        error.innerHTML = 'HTTP error: ' + commentsResponse.status;
        comment_list.appendChild(error);
        return;
    }

    const comments = await commentsResponse.json();
    for(const comment in comments) {
            console.log(comments[comment]);
        const new_card = create_comment_element(comments[comment]);
        comment_list.appendChild(new_card);
    }

}

create_comments();