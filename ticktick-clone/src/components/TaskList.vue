<template>
  <div>
    <a-list bordered :data-source="tasksToDisplay" :loading="tasksStore.isLoading">
      <template #renderItem="{ item }">
        <TaskItem
          :task="item"
          @update:taskCompletion="handleTaskCompletionUpdate"
          @editTask="handleEditTask"
          @deleteTask="handleDeleteTask"
        />
      </template>
      <div v-if="!tasksStore.isLoading && tasksToDisplay.length === 0" style="text-align: center; padding: 20px;">
        <a-empty :description="emptyDescription" />
      </div>
      <div v-if="tasksStore.error" style="padding: 20px;">
        <a-alert
          message="Error Loading Tasks"
          :description="tasksStore.error.message || 'An unexpected error occurred while fetching tasks.'"
          type="error"
          show-icon
        />
      </div>
    </a-list>
  </div>
</template>

<script setup>
import { onMounted, computed, defineEmits } from 'vue';
import TaskItem from './TaskItem.vue';
import { useTasksStore } from '@/store/tasksStore';
import { useAppStore } from '@/store/appStore';
import { useProjectsStore } from '@/store/projectsStore';
import { List as AList, Empty as AEmpty, Alert as AAlert, message } from 'ant-design-vue'; // Import AAlert and message

const tasksStore = useTasksStore();
const appStore = useAppStore();
const projectsStore = useProjectsStore();

// Define emits for this component
const emit = defineEmits(['requestEditTask']);

onMounted(() => {
  if (tasksStore.tasks.length === 0) tasksStore.fetchTasks();
  if (projectsStore.projects.length === 0) projectsStore.fetchProjects();
});

const tasksToDisplay = computed(() => {
  if (appStore.selectedType === 'project') {
    return tasksStore.getTasksByProjectId(appStore.selectedKey);
  } else if (appStore.selectedType === 'smartlist') {
    if (appStore.selectedKey === 'today') {
      return tasksStore.getTasksDueToday; // Use the getter
    } else if (appStore.selectedKey === 'next7days') {
      return tasksStore.getTasksDueNext7Days; // Use the getter
    }
  }
  return [];
});

const emptyDescription = computed(() => {
  if (tasksStore.isLoading) return "Loading tasks...";
  if (appStore.selectedType === 'project') {
    const project = projectsStore.getProjectById(appStore.selectedKey);
    return project ? `No tasks in ${project.name}.` : 'Select a project to see tasks.';
  } else if (appStore.selectedType === 'smartlist') {
    if (appStore.selectedKey === 'today') return 'No tasks due today.';
    if (appStore.selectedKey === 'next7days') return 'No tasks due in the next 7 days.';
    return 'No tasks for this view.';
  }
  return 'No tasks to display.';
});

const handleTaskCompletionUpdate = ({ id, isCompleted }) => {
  tasksStore.updateTaskCompletion({ id, isCompleted });
  // Smart lists might need re-evaluation, Pinia's reactivity should handle this.
};

const handleEditTask = (task) => {
  // Emit an event up to MainContent to open the modal
  emit('requestEditTask', task);
};

const handleDeleteTask = (taskId) => {
  tasksStore.deleteTask(taskId);
  message.success('Task deleted successfully.');
};
</script>
