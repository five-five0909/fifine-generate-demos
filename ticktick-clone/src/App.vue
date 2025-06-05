// src/App.vue
<template>
  <a-layout style="min-height: 100vh">
    <Sidebar />
    <a-layout>
      <MainContent />
    </a-layout>
  </a-layout>
</template>

<script setup>
import { onMounted } from 'vue';
import Sidebar from './components/Sidebar.vue';
import MainContent from './components/MainContent.vue';
import { useProjectsStore } from '@/store/projectsStore';
import { useTasksStore } from '@/store/tasksStore';
import { Layout as ALayout } from 'ant-design-vue';

const projectsStore = useProjectsStore();
const tasksStore = useTasksStore();

onMounted(async () => {
  // Fetch initial data if stores are empty
  // Components also do this, but this can be a central place
  if (projectsStore.projects.length === 0) {
    await projectsStore.fetchProjects();
  }
  if (tasksStore.tasks.length === 0) {
    await tasksStore.fetchTasks();
  }
});
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
