<template>
    <div>
        <h4 class="alert alert-success text-left"> Добавление задачи </h4>
        <!-- Наша формочка -->
        <form @submit.prevent="onSubmit">
            <!-- Тут сообщение об ошибке -->
            <b-alert variant="danger"
                     :show="dismissCountDown"
                     dismissible fade
                     @dismiss-count-down="countDownChanged">
                {{ textAlert }}
            </b-alert>
            <!-- Тут сообщение, что все хорошо -->
            <b-alert variant="success"
                     :show="dismissCountDownGood"
                     dismissible fade
                     @dismiss-count-down="countDownChangedGood">
                {{ textAlert }}
            </b-alert>

            <div class="form-group">
                <label for="title-input"> Задача: </label>
                <input type="text" class="form-control" id="title-input" placeholder="Название задачи" v-model="title">
            </div>

            <div class="form-group">
                <label for="description-input"> Описание задачи: </label>
                <textarea class="form-control" id="description-input" placeholder="Описание задачи"
                       v-model="description"> </textarea>
            </div>

            <button type="submit" class="btn btn-primary text-center"> Добавить </button>
        </form>
    </div>
</template>

<script>

    export default {
        name: "AddTodo",
        data() {
            return {
                title: '',
				textAlert: '',
                description: '',
                dismissSecs: 4,
                dismissCountDown: 0,
				dismissSecsGood: 4,
				dismissCountDownGood: 0
            }
        },
        computed: {
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
					this.textAlert = 'Задача успешно добавлена';
				}
				else {
					this.dismissCountDownGood = 0;
					this.dismissCountDown = this.dismissSecs;
					this.textAlert = this.getError;
				}
			},
            async onSubmit() {
                const title = this.title === undefined ? '' : this.title.trim();
                const description = this.description === undefined ? '' : this.description.trim();

                if (title === '') {
                    await this.$store.dispatch('SET_ERROR', {type: 'add', error: 'No name todo'});
                    this.showAlert(false);
                    return;
                }

                await fetch(`http://127.0.0.1:7070/api/todos/`, {
                    method: 'POST',
                    body: JSON.stringify({
                        title: title,
                        is_complete: false,
                        description: description
                    }),
                    headers: {
                        "Content-type": "application/json; charset=UTF-8"
                    }
                })
                    .then(response => {
                        if (response.status === 201) {
                            response.json()
                                .then(json => {
                                    this.$store.dispatch('ADD_TODO', json);
                                    this.title = '';
                                    this.description = '';
                                    this.showAlert(true)
                                });
                        } else {
                            response.json()
                                .then(json => {
                                    this.$store.dispatch('SET_ERROR', {type: 'add', error: json.error});
                                    this.showAlert(false);
                                });
                        }
                    })
                    .catch(error => {
                        this.$store.dispatch('SET_ERROR', {type:'add', error: error});
                        this.showAlert(false);
                    });
            }
        }
    }
</script>

<style>

    button {
        float: left;
    }

    label {
        float: left;
    }

</style>