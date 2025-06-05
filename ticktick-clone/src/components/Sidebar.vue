<template>
  <a-layout-sider collapsible width="250">
    <div class="logo" style="height: 32px; background: rgba(255, 255, 255, 0.2); margin: 16px; display: flex; align-items: center; justify-content: center; color: white; font-size: 18px;">
      My Tasks
    </div>

    <div style="display: flex; justify-content: space-between; align-items: center; padding: 0 16px 0 24px; margin-top: 20px;">
      <a-typography-title :level="5" style="color:rgba(255, 255, 255, 0.65); margin-bottom: 0;">Projects</a-typography-title>
      <a-button type="text" @click="showAddProjectModal" style="color: rgba(255,255,255,0.65); font-size: 18px;">
        <PlusCircleOutlined />
      </a-button>
    </div>
    <ProjectList @projectSelected="handleProjectSelect" :selectedProjectId="appStore.currentSelectedProjectId" />

    <a-divider style="border-color: rgba(255,255,255,0.2);" />

    <a-typography-title :level="5" style="color:rgba(255, 255, 255, 0.65); padding-left: 24px; margin-top:10px;">Smart Lists</a-typography-title>
    <a-menu
      theme="dark"
      mode="inline"
      :selectedKeys="appStore.selectedType === 'smartlist' ? [appStore.selectedKey] : []"
      @click="handleSmartListClick"
      :items="smartListMenuItems"
    >
    </a-menu>

    <AddProjectModal
      :visible="isAddProjectModalVisible"
      @close="handleAddProjectModalClose"
      @projectAdded="handleProjectAdded"
    />
  </a-layout-sider>
</template>

<script setup>
import { ref, computed } from 'vue';
import ProjectList from './ProjectList.vue';
import AddProjectModal from './modals/AddProjectModal.vue';
import { useAppStore } from '@/store/appStore';
    import { message } from 'ant-design-vue'; // Import message
import {
  CalendarOutlined,
  BarChartOutlined,
  PlusCircleOutlined,
  AppstoreOutlined // Used by ProjectList, ensure it's available if ProjectList changes
} from '@ant-design/icons-vue';
import { LayoutSider as ALayoutSider, Menu as AMenu, Button as AButton, Divider as ADivider, TypographyTitle as ATypographyTitle } from 'ant-design-vue';

const appStore = useAppStore();

const isAddProjectModalVisible = ref(false);

const showAddProjectModal = () => {
  isAddProjectModalVisible.value = true;
};
const handleAddProjectModalClose = () => {
  isAddProjectModalVisible.value = false;
};
const handleProjectAdded = () => {
      message.success('Project added successfully!');
};

const handleProjectSelect = (projectId) => {
  appStore.setSelectedKey(projectId, 'project');
};

const smartListMenuItems = ref([
    { key: 'today', label: 'Today', icon: () => <CalendarOutlined /> },
    { key: 'next7days', label: 'Next 7 Days', icon: () => <BarChartOutlined /> }
]);

const handleSmartListClick = (e) => {
  appStore.setSelectedKey(e.key, 'smartlist');
};
</script>
