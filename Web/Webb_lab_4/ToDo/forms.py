from django import forms


class LoginForm(forms.Form):
    username = forms.CharField(label="Логин",
                               widget=forms.TextInput(attrs={'class': 'form-control'}))
    password = forms.CharField(label="Пароль",
        widget=forms.PasswordInput(attrs={'class': 'form-control'}))


class NewTodo(forms.Form):
    name = forms.CharField(label="Название")
    description = forms.CharField(widget=forms.Textarea, label="Описание")

