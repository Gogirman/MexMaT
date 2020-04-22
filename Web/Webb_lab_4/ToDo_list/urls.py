"""ToDo_list URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin
from django.urls import path, re_path
from ToDo import views

urlpatterns = [
    path('', views.login_user),
    path('admin/', admin.site.urls),
    path('login.html', views.login_user),
    path('error.html', views.err),
    path('registration.html', views.registration),
    path('todos.html', views.todos),
    path('create_new_todo.html', views.create_new_todo),
    path('delete_todo/<int:todo_id>/', views.delete_todo_ajax),
    path('edit_todo/<int:todo_id>/', views.edit_todo),
    path('change_check/<int:todo_id>/', views.change_check),
    path('log_out/', views.log_out),
    path('todos/', views.log_out),
    re_path('back_todos_list/$', views.back_todos),

]
