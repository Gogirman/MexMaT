import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

const store = new Vuex.Store({
	// Состояние приложения
    state: {
		// Тудушки
        todos: [],
        todo: {},
		// Текущие ошибки
        addError: null,
        editError: null,
        notFoundError: null,
        initError: null,
        delError: null,
        editStatusError: null,
    },

    // Геттеры возвращают данные из хранилища
	getters: {
        // Все тудушки
        TODOS: state => {
            return state.todos;
        },
        // Выполненные тудушки
        COMPL_TODOS: state => {
            return state.todos.filter(t => t.completed === true);
        },
        // Не выполненые тудушки
        NOT_COMPL_TODOS: state => {
            return state.todos.filter(t => t.completed === false);
        },
        // Тудушки по условию
        SEARCH_TODOS: state => (title) => {
            return state.todos.filter(t => t.title.indexOf(title) > -1);
        },
        // Состояние приложения
        TODO: state => {
            return state.todo;
        },

        // Ошибки
        addError: state => {
            return state.addError;
        },
        editError: state => {
            return state.editError;
        },
        notFoundError: state => {
            return state.notFoundError;
        },
        initError: state => {
            return state.initError;
        },
        delError: state => {
            return state.initError;
        },
        editStatusError: state => {
            return state.editStatusError;
        },
    },

    // Мутации устанавливают данные в хранилище (сеттеры)
    // payload — данные, передаваемые нашей мутации из компонента
    mutations: {
        SET_TODOS: (state, payload) => {
            state.todos = payload;
        },
        SET_TODO: (state, payload) => {
            state.todo = payload;
        },
        SET_ERROR: (state, {type, error}) => {
            switch (type) {
                case 'add': {
                    state.addError = error;
                    break;
                }
                case 'edit': {
                    state.editError = error;
                    break;
                }
                case 'notFound': {
                    state.notFoundError = error;
                    break;
                }
                case 'init': {
                    state.initError = error;
                    break;
                }
                case 'del': {
                    state.delError = error;
                    break;
                }
                case 'editStatus': {
                    state.editStatusError = error;
                    break;
                }
            }
        },

        ADD_TODO: (state, payload) => {
            state.todos.push(payload);
        },

        EDIT_TODO: (state, {idx, todo}) => {
            state.todos.splice(idx, 1, todo);
        },

        DELETE_TODO: (state, payload) => {
            state.todos.splice(payload, 1);
        },
    },

    actions: {
		// Действие получения всех тудушек
         FETCH_TODOS: ({commit}) => {
            return fetch('http://localhost:3000/todos')
                .then(response => {
                    if (response.status === 200) {
                        return response.json()
                            .then(json => {
                                commit('SET_TODOS', json);
                            });
                    }
                })
                .catch(error => {
                    console.log(error);
                    commit('SET_ERROR', {type:'init', error: error.message});
                });
        },


        SET_ERROR: ({commit}, payload) => {
            commit('SET_ERROR', payload);
        },

        SET_TODO: async ({commit}, payload) => {
            commit('SET_TODO', payload);
        },

        ADD_TODO: async ({commit}, payload) => {
            commit('ADD_TODO', payload);
        },

        EDIT_TODO: async ({commit}, payload) => {

            commit('EDIT_TODO', payload);
        },

        DELETE_TODO: async ({commit}, payload) => {
            commit('DELETE_TODO', payload);
        }

    },
});
export default store;