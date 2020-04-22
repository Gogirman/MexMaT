<template>
    <form class="container w-100 mx-auto">
        <h4 class="alert alert-info text-center"> Информация о задаче: </h4>

        <form class="card-body">
            <b-alert class="alert alert-danger"
                     :show="dismissCountDown"
                     @dismissed="dismissCountDown=0"
                     @dismiss-count-down="countDownChanged">
                {{ getError }}
            </b-alert>

            <Loader v-if="loading"></Loader>

            <form v-else @submit.prevent="onSubmit">

                <div class="form-group">
                    <label for="title-input"> Заголовок: </label>
                    <input type="text" name="title" class="form-control" id="title-input" placeholder="Заголовок"
                           v-model="title">
                </div>

                <div class="form-group">
                    <label for="description-input"> Описание задачи: </label>
                    <textarea class="form-control" id="description-input" placeholder="Описание задачи"
                              v-model="description"> </textarea>
                </div>

                <div class="form-group">
                    <label> Статус: </label>
                    <select v-model="is_complete" class="form-control for-margin" aria-label="Basic example">
                        <option value="true" selected>Выполнено</option>
                        <option value="false">Не выполнено</option>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary text-center"> Изменить данные </button>

            </form>

        </form>

    </form>
</template>

<script>
    import Loader from '../Loader.vue'

    export default {
        name: "GetTodoDescription",
        data() {
            return {
                title: '',
                description: '',
                is_complete: false,
                id: Number(this.$route.params.id),
                loading: true,
                seen: false,
                dismissSecs: 3,
                dismissCountDown: 0
            }
        },
        components: {
            Loader,
        },
        computed: {
            getTodo() {
                return this.$store.getters.TODO;
            },
            todoList() {
                return this.$store.getters.TODOS;
            },
            getError() {
                return this.$store.getters.editError;
            },

        },
        async created() {
            if (this.todoList.find(t => t.id === this.id)) {
                const todo = this.todoList.find(t => t.id === this.id);
                await this.$store.dispatch('SET_TODO', todo);
                this.title = todo.title;
                this.description = todo.description;
                this.is_complete = todo.is_complete;
            } else {
                await this.$store.dispatch('SET_ERROR', {type: 'add', error: 'Task does not exist'});
                await this.$router.push({path: '/todos'});
            }
            this.loading = false;
        },
        methods: {
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            },

            async onSubmit() {
                const title = this.title === undefined ? '' : this.title.trim();
                const description = this.description === undefined ? '' : this.description.trim();
                const is_complete = this.is_complete === 'true' ? true : false;

                this.loading = true;
                await fetch(`http://127.0.0.1:7070/api/todos/${this.getTodo.id}`, {
                    method: 'PATCH',
                    body: JSON.stringify({
                        title: title,
                        description: description,
                        is_complete: is_complete
                    }),
                    headers: {
                        "Content-type": "application/json; charset=UTF-8"
                    }
                })
                    .then(response => {
                        if (response.status < 400) {
                            response.json()
                                .then(json => {
                                    this.$store.dispatch('EDIT_TODO', {
                                        idx: this.todoList.indexOf(this.getTodo),
                                        todo: json
                                    });
                                    this.$router.push({path: '/todos'});
                                });
                        } else {
                            response.json()
                                .then(json => {
                                    this.$store.dispatch('SET_ERROR', {type: 'edit', error: json.error});
                                    this.showAlert();
                                });
                        }
                    })
                    .catch(error => {
                        this.$store.dispatch('SET_ERROR', {type: 'edit', error: error});
                        this.showAlert();
                    });
                this.loading = false;
            }
        }
    }
</script>

<style scoped>

    label {
        float: left;
    }

    button {
        float: left;
    }

    .alert {
        margin-bottom: 0;
    }

    .form-group {
        margin-bottom: 1.25rem;
    }

    .card-body {
        padding: 1.25rem 0 1.25rem 0;
    }

</style>