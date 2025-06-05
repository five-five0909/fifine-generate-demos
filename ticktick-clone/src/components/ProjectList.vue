<template>
  <a-menu
    theme="dark"
    mode="inline"
    :selectedKeys="selectedProjectId ? [selectedProjectId] : []"
    @click="handleMenuClick"
    :items="menuItems"
  >
  </a-menu>
  <div v-if="projectsStore.isLoading" style="text-align: center; padding: 10px;">
    <a-spin />
  </div>
  <div v-if="projectsStore.error" style="padding: 10px;">
    <a-alert
      message="Error Loading Projects"
      :description="projectsStore.error.message || 'An unexpected error occurred while fetching projects.'"
      type="error"
      show-icon
    />
  </div>
</template>

<script setup>
import { onMounted, computed, defineProps, defineEmits } from 'vue';
import { useProjectsStore } from '@/store/projectsStore';
import { AppstoreOutlined } from '@ant-design/icons-vue';
import { Menu as AMenu, Spin as ASpin, Alert as AAlert } from 'ant-design-vue'; // Import AAlert

const props = defineProps({
  selectedProjectId: String
});
const emit = defineEmits(['projectSelected']);

const projectsStore = useProjectsStore();

onMounted(() => {
  if (projectsStore.projects.length === 0) {
    projectsStore.fetchProjects();
  }
});

const menuItems = computed(() =>
  projectsStore.projects.map(project => ({
    key: project.id,
    label: project.name,
    icon: () => <AppstoreOutlined />,
  }))
);

const handleMenuClick = (e) => {
  emit('projectSelected', e.key);
};
</script>
