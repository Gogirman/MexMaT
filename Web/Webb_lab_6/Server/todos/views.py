from rest_framework.generics import ListCreateAPIView, RetrieveUpdateDestroyAPIView
from .models import Todo
from .serializers import TodoSerializer

class TodosView(ListCreateAPIView):
    queryset = Todo.objects.all()
    serializer_class = TodoSerializer

class SingleTodoView(RetrieveUpdateDestroyAPIView):
    queryset = Todo.objects.all()
    serializer_class = TodoSerializer