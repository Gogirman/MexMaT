<template>
<!--    class="wallp"-->
    <div id="app" >

        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <img src="@/images/kotik.jpg" alt="Котик" class="header-img">
            <a class="navbar-brand" href="#">TODOшечка</a>
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <keep-alive>
                        <router-link class="nav-link" to="/">Домой</router-link>
                    </keep-alive>
                </li>

                <li class="nav-item">
                    <keep-alive>
                        <router-link class="nav-link" to="/todos">Задачи</router-link>
                    </keep-alive>
                </li>

                <li class="nav-item">
                    <keep-alive>
                        <router-link class="nav-link" to="/addtodo">Добавить задачу</router-link>
                    </keep-alive>
                </li>
            </ul>
        </nav>

        <router-view>
            <!-- отображаем тут компонент, для которого совпадает маршрут -->
        </router-view>

    </div>
</template>

<script>
    export default {
        name: 'app',

    }
</script>

<style>
    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        padding: 0;
        margin: 0;
    }

    nav {
        margin-bottom: 2rem;
    }

    .wallp {
        min-height: 1000px;
        background: url(images/wall.jpg) no-repeat fixed left bottom;
        background-size: cover;
    }

    .header-img {
        width: 100px;
        height: 100px;
        border-radius: 25% 25% 25% 0;
        border: 2px solid;
        float: left;
        margin: 1%;
    }


</style>
