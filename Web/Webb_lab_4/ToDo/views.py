from django.shortcuts import render
from .forms import LoginForm, NewTodo
from .models import Todo
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.models import User
from django.http import HttpResponsePermanentRedirect
from django.http import JsonResponse


# Create your views here.
def login_user(request):
    if request.method == 'POST':
        form = LoginForm(request.POST)
        if form.is_valid():
            cd = form.cleaned_data
            user = authenticate(username=cd['username'], password=cd['password'])
            if user is not None:
                login(request, user)
                return HttpResponsePermanentRedirect("todos.html")
            else:
                return HttpResponsePermanentRedirect("error.html")
    else:
        form = LoginForm()
        data = {'form': form, 'hello_text': "Вход"}
    return render(request, 'login.html', data)


def registration(request):
    if request.method == 'POST':
        form = LoginForm(request.POST)
        if form.is_valid():
            cd = form.cleaned_data
            user = User.objects.create_user(username=cd['username'], password=cd['password'])
            user.save()
            return HttpResponsePermanentRedirect("login.html")
        else:
            return HttpResponsePermanentRedirect("error.html")
    else:
        form = LoginForm()
        data = {'form': form, 'hello_text': "Регистрация"}
    return render(request, 'registration.html', data)


def todos(request):
    user = request.user.username
    all_todo = Todo.objects.filter(login=user)
    data = {'login': user, 'todos': all_todo, 'hello_text': 'Ура, настало время задач'}
    return render(request, 'todos.html', data)


def change_check(request, todo_id):
    if request.method == 'GET':
        if request.is_ajax():
            try:
                todo = Todo.objects.get(id=todo_id)
                todo.is_complete = not todo.is_complete
                todo.save()
            except Exception:
                return JsonResponse({"success": False, "todo_id": todo_id}, status=400)
            else:
                return JsonResponse({"success": True, "todo_id": todo_id}, status=200)


def create_new_todo(request):
    user = request.user.username
    if request.method == 'POST':
        form = NewTodo(request.POST)
        if form.is_valid():
            cd = form.cleaned_data
            new_todo = Todo.objects.create(login=user, name=cd['name'], description=cd['description'],
                                           is_complete=False)
            new_todo.save()
            return HttpResponsePermanentRedirect("todos.html")
        else:
            return HttpResponsePermanentRedirect("error.html")
    else:
        form1 = NewTodo()
        data = {
            'hello_text': "Новая задача",
            'form1': form1,
        }
        return render(request, 'create_new_todo.html', data)


def delete_todo(request, todo_id):
    todo = Todo.objects.get(id=todo_id)
    todo.delete()
    return HttpResponsePermanentRedirect("/todos.html")


def delete_todo_ajax(request, todo_id):
        if request.method == 'GET':
            if request.is_ajax():
                try:
                    todo = Todo.objects.get(id=todo_id)
                    todo.delete()
                except Exception:
                    return JsonResponse({"success": False, "todo_id": todo_id}, status=400)
                else:
                    return JsonResponse({"success": True, "todo_id": todo_id}, status=200)


def edit_todo(request, todo_id):
    todo = Todo.objects.get(id=todo_id)
    user = request.user.username
    if todo not in Todo.objects.filter(login=user):
        return HttpResponsePermanentRedirect("/todos.html")
    return render(request, "edit_todo.html", {"todo": todo, 'hello_text': "Просмотр задачи"})


def log_out(request):
    logout(request)
    return HttpResponsePermanentRedirect("/")


def back_todos(request):
    return HttpResponsePermanentRedirect("/todos.html")


def err(request):
    return render(request, 'error.html')
