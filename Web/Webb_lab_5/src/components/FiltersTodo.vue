<template>
    <div id="section-container">
        <h4 class="alert alert-info text-left"> Список задач: </h4>

        <div>
            <form>
                <label for="filter">Фильтр задач:</label>

                <input type="text"
                       class="form-control for-margin"
                       id="filter"
                       placeholder="Заголовок задач"
                       v-model="filterTitle" v-on:input="findByTitle">

                <select v-model="filter"
                        class="form-control for-margin"
                        aria-label="Basic example"
                        @change="onChangeSelect">

                    <option value="all">Все задачи</option>
                    <option value="finished">Завершенные</option>
                    <option value="unfinished">Не завершенные</option>
                </select>
            </form>
        </div>

        <Loader v-if="loading"></Loader>

        <div v-else class="card card-default my-style mx-auto col-sm">
            <TodoList v-if="filteredTodos !== undefined && filteredTodos.length"
                      v-bind:todos="filteredTodos"/>

            <p v-else > Нет задач </p>

        </div>
    </div>
</template>

<script>
    import TodoList from './TodoList.vue'
    import TodoItem from './TodoItem.vue'
    import Loader from './Loader.vue'

    export default {
        name: 'FiltersTodo',
        data() {
            return {
				filter: 'all',
                filterTitle: '',
                loading: true,
				filteredTodos: this.todoList
            }
        },
        components: {
            TodoList,
            TodoItem,
            Loader
        },
         async created() {
            if (this.todoList.length === 0) {
                await this.$store.dispatch('FETCH_TODOS');
            }
            this.loading = false;
			this.onChangeSelect();
        },
        computed: {
            todoList() {
                return this.$store.getters.TODOS;
            }
        },
		methods: {
			onChangeSelect()
			{
				if (this.filter === 'all') {
					this.filteredTodos = this.todoList;
				}
				if (this.filter === 'finished') {
					this.filteredTodos = this.$store.getters.COMPL_TODOS;
				}
				if (this.filter === 'unfinished') {
					this.filteredTodos = this.$store.getters.NOT_COMPL_TODOS;
				}
			},
        	findByTitle() {
				this.filteredTodos = this.filterTitle === undefined ? this.todoList
							: this.$store.getters.SEARCH_TODOS(this.filterTitle.trim());
			}
		}
    }
</script>

<style scoped>

    .for-margin {
        margin-bottom: 0.5rem;
    }

    li {
        border: 1px solid #ccc;
        display: flex;
        justify-content: space-between;
        padding: 0.5rem 2rem;
        margin-bottom: 0.5rem;
    }

    label {
        float: left;
    }

</style>