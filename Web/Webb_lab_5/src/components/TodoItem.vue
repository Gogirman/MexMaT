<template>
	<div>
		<Loader v-if="loading"/>

		<li v-else>
			<!-- Отображение еррора -->
			<b-alert variant="danger"
					 :show="dismissCountDown"
					 dismissible fade
					 @dismiss-count-down="countDownChanged">
				{{ textAlert }}
			</b-alert>

			<span>
				<label>
					<input v-model="todo.completed === true"
						   type="checkbox"
						   class="my-checkbox"
						   v-on:change="editStatusTodo()">
				</label>

				<keep-alive>
					<router-link tag="a" :to="{ name: 'todoId', params: { id: todo.id} }">{{todo.title}}</router-link>
				</keep-alive>

			</span>

			<button class="my-button-delete btn btn-danger"
					v-on:click="removeTodoClicked(todo.id)">
				Удалить
			</button>
		</li>
	</div>
</template>


<script>
    import Loader from './Loader.vue'

    export default {
        name: 'TodoItem',
        props: {
            todo: {
                type: Object,
                required: true
            }
        },
        data() {
            return {
            	error: false,
                loading: false,
				textAlert: '',
				dismissSecs: 4,
				dismissCountDown: 0,
				dismissSecsGood: 4,
				dismissCountDownGood: 0
            }
        },
        components: {
            Loader
        },
        computed: {
            todoList() {
                return this.$store.getters.TODOS;
            },
			getError() {
				return this.$store.getters.addError;
			}
        },
        methods: {
			countDownChanged(dismissCountDown) {
				this.dismissCountDown = dismissCountDown
			},
			countDownChangedGood(dismissCountDownGood) {
				this.dismissCountDownGood = dismissCountDownGood;
			},
			showAlert(good) {
				if (good) {
					this.dismissCountDown = 0;
					this.dismissCountDownGood = this.dismissSecs;
					this.textAlert = 'Task successfully removed';
				} else {
					this.dismissCountDownGood = 0;
					this.dismissCountDown = this.dismissSecs;
					this.textAlert = this.getError;
				}
			},
			// Удаление тудушки
            async removeTodoClicked(id) {
                if (!confirm('Are you sure?')) {
                    return;
                }
                this.loading = true;
                await fetch(`http://localhost:3000/todos/${id}`, {
                    method: 'DELETE',
                })
                    .then(response => {
                        if (response.status === 200) {
                            response.json()
                                .then(() => {
                                	console.log(this.todoList.findIndex(t => t.id === id));
                                    this.$store.dispatch('DELETE_TODO', this.todoList.findIndex(t => t.id === id));
                                });
                        } else {
                            response.json()
                                .then(json => {
                                    this.$store.dispatch('SET_ERROR', {type: 'del', error: json.error});
                                    this.showAlert(false);
                                });
                        }
                    })
                    .catch(error => {
                        this.$store.dispatch('SET_ERROR', {type: 'del', error: error});
                        this.showAlert(false);
                    });

                this.loading = false;
            },

			// Изменение статуса тудушки
            async editStatusTodo() {
                await fetch(`http://localhost:3000/todos/${this.todo.id}`, {
                    method: 'PATCH',
                    body: JSON.stringify({
						completed: !this.todo.completed
                    }),
                    headers: {
                        "Content-type": "application/json; charset=UTF-8"
                    },
                })
                    .then(response => {
                        if (response.status === 200) {
                            response.json()
                                .then(json => {
									console.log(json);
                                    this.$store.dispatch('EDIT_TODO', {
                                        idx: this.todoList.indexOf(this.todo),
                                        todo: json
                                    });
                                });
                        }
                    })
                    .catch(error => {
                        this.$store.dispatch('SET_ERROR', {type: 'editStatus', error: error});
                        this.showAlert(false);
                    });
            }
        }
    }
</script>


<style scoped>

    span {
        display: flex;
        align-items: center;
    }

    li {
        border: 1px solid #ccc;
        padding: 0.5rem 1rem;
        display: flex;
        align-items: center;
        margin-top: 0.5rem;
        margin-bottom: 0.5rem;
    }

    .my-checkbox {
        display: flex;
        align-items: center;
        margin-right: 0.5rem;
    }

    .my-button-delete {
        margin-left: auto;
        padding-left: 1rem;
    }
</style>