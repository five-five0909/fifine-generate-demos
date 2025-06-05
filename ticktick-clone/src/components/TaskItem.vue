<template>
  <a-list-item :class="{ 'completed-task': task.isCompleted }">
    <a-list-item-meta>
      <template #avatar>
        <a-checkbox v-model:checked="isTaskCompleted" @change="onTaskCompletionChange"></a-checkbox>
      </template>
      <template #title>
        <span :style="{ textDecoration: task.isCompleted ? 'line-through' : 'none', color: task.isCompleted ? '#aaa' : 'inherit' }">
          {{ task.title }}
        </span>
      </template>
      <template #description v-if="task.description">
        <span :style="{ color: task.isCompleted ? '#ccc' : 'inherit' }">
          {{ task.description }}
        </span>
      </template>
    </a-list-item-meta>
    <template #actions>
          <a-tag v-if="task.priority" :color="getPriorityColor(task.priority)">{{ task.priority.charAt(0).toUpperCase() + task.priority.slice(1) }}</a-tag>
      <a-tag v-if="task.dueDate" :color="getDateTagColor(task.dueDate, task.isCompleted)">{{ formattedDueDate }}</a-tag>
      <a-tooltip title="Edit Task">
        <a-button type="link" @click="onEditClick" :disabled="task.isCompleted">
          <EditOutlined />
        </a-button>
      </a-tooltip>
      <a-popconfirm
        title="Are you sure you want to delete this task?"
        ok-text="Yes"
        cancel-text="No"
        @confirm="onDeleteConfirm"
      >
        <a-tooltip title="Delete Task">
          <a-button type="link" danger>
            <DeleteOutlined />
          </a-button>
        </a-tooltip>
      </a-popconfirm>
    </template>
  </a-list-item>
</template>

<script setup>
import { computed, defineProps, defineEmits } from 'vue';
import { EditOutlined, DeleteOutlined } from '@ant-design/icons-vue';
import dayjs from 'dayjs';
import { Checkbox as ACheckbox, List as AList, ListItem as AListItem, ListItemMeta as AListItemMeta, Tag as ATag, Button as AButton, Popconfirm as APopconfirm, Tooltip as ATooltip } from 'ant-design-vue';


const props = defineProps({
  task: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(['update:taskCompletion', 'editTask', 'deleteTask']);

// Local computed property for checkbox to avoid directly mutating prop
const isTaskCompleted = computed({
    get: () => props.task.isCompleted,
    set: (value) => {
        // This is just to satisfy v-model, the actual update is via @change
    }
});

const onTaskCompletionChange = (e) => {
  emit('update:taskCompletion', { id: props.task.id, isCompleted: e.target.checked });
};

const onEditClick = () => {
  emit('editTask', props.task);
};

const onDeleteConfirm = () => {
  emit('deleteTask', props.task.id);
};

const formattedDueDate = computed(() => {
    if (!props.task.dueDate) return '';
    return dayjs(props.task.dueDate).format('MMM D');
});

const getDateTagColor = (dueDate, isCompleted) => {
  if (isCompleted) return 'grey';
  if (!dueDate) return '';
  const today = dayjs().startOf('day');
  const due = dayjs(dueDate);
  if (due.isBefore(today)) return 'red';
  if (due.isSame(today, 'day')) return 'orange';
  return 'blue';
};

const getPriorityColor = (priority) => {
  if (priority === 'high') return 'volcano';
  if (priority === 'medium') return 'geekblue';
  if (priority === 'low') return 'green';
  return 'default';
};
</script>

<style scoped>
.ant-list-item-meta-title span,
.ant-list-item-meta-description span {
  transition: color 0.3s;
}
.completed-task .ant-list-item-meta-title span,
.completed-task .ant-list-item-meta-description span {
   /* Styles for completed tasks are now inline for simplicity */
}
/* Disable interactions on completed task's main content except checkbox */
.completed-task .ant-list-item-meta-title,
.completed-task .ant-list-item-meta-description {
    pointer-events: none;
}
.ant-list-item-meta-avatar { /* Allow checkbox interaction */
    pointer-events: auto;
}
</style>
