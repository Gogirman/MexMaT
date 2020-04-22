from django.urls import path
from .views import TodosView, SingleTodoView
app_name = "Todos"
# app_name will help us do a reverse look-up latter.
urlpatterns = [
    path('todos/', TodosView.as_view()),
    path('todos/<int:pk>', SingleTodoView.as_view()),
]