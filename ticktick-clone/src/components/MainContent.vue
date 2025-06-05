<template>
  <a-layout-content style="margin: 0;">
    <div style="padding: 24px; background: #fff; min-height: 100%;">
      <a-page-header
        :title="currentViewTitle"
        :sub-title="currentViewSubTitle"
        style="background: #f5f5f5; padding: 16px 24px; margin-bottom: 24px;"
      >
        <template #extra>
          <a-button
            type="primary"
            @click="showAddTaskModal"
            :disabled="appStore.selectedType === 'smartlist' && !canAddSmartListTask(appStore.selectedKey)"
          >
            <PlusOutlined /> Add Task
          </a-button>
        </template>
      </a-page-header>

      <TaskList @requestEditTask="showEditTaskModal" />

      <TaskFormModal
        :visible="isModalVisible"
        :taskToEdit="taskBeingEdited"
        :defaultProjectId="appStore.currentSelectedProjectId"
        @close="handleModalClose"
        @taskSaved="handleTaskSaved"
      />
    </div>
  </a-layout-content>
</template>

<script setup>
import { ref, computed } from 'vue';
import TaskList from './TaskList.vue';
import TaskFormModal from './modals/TaskFormModal.vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { useAppStore } from '@/store/appStore';
import { useProjectsStore } from '@/store/projectsStore';
import { message } from 'ant-design-vue'; // For user feedback
import { LayoutContent as ALayoutContent, PageHeader as APageHeader, Button as AButton } from 'ant-design-vue';


const appStore = useAppStore();
const projectsStore = useProjectsStore();

const currentViewTitle = computed(() => { /* ... existing ... */
    if (appStore.selectedType === 'project') {
        const project = projectsStore.getProjectById(appStore.selectedKey);
        return project ? project.name : 'Tasks';
    }
    if (appStore.selectedType === 'smartlist') {
        if (appStore.selectedKey === 'today') return 'Today';
        if (appStore.selectedKey === 'next7days') return 'Next 7 Days';
        return 'Smart List';
    }
    return 'Tasks';
});
const currentViewSubTitle = computed(() => { /* ... existing ... */
    if (appStore.selectedType === 'project') {
        return `All tasks in ${currentViewTitle.value}`;
    }
    if (appStore.selectedType === 'smartlist') {
        return `Tasks due ${appStore.selectedKey.toLowerCase()}`;
    }
    return 'Manage your tasks';
});

const isModalVisible = ref(false);
const taskBeingEdited = ref(null); // To store the task being edited

// Determine if tasks can be added to the current smart list view
const canAddSmartListTask = (smartListKey) => {
    // Example: Allow adding to 'Today' if it auto-sets due date.
    // For now, disable for all smart lists.
    return false;
};

const showAddTaskModal = () => {
  taskBeingEdited.value = null; // Ensure we are in "add" mode
  if (appStore.selectedType === 'project' && appStore.selectedKey) {
    isModalVisible.value = true;
  } else if (appStore.selectedType === 'smartlist' && canAddSmartListTask(appStore.selectedKey)) {
    isModalVisible.value = true; // Modal needs to handle smart list defaults
  } else {
    message.info('Select a project to add a task, or this smart list does not support adding tasks directly.');
  }
};

const showEditTaskModal = (task) => {
  taskBeingEdited.value = task;
  isModalVisible.value = true;
};

const handleModalClose = () => {
  isModalVisible.value = false;
  taskBeingEdited.value = null; // Clear task being edited
};

const handleTaskSaved = () => {
  message.success(taskBeingEdited.value ? 'Task updated successfully!' : 'Task added successfully!');
  // TaskList will update due to store reactivity.
  // Modal is closed by itself on successful save.
};

if (projectsStore.projects.length === 0) projectsStore.fetchProjects();
</script>
<style scoped>
.ant-layout-content { margin: 0 !important; }
</style>
