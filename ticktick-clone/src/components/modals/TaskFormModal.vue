<template>
  <a-modal
    :visible="visible"
    :title="modalTitle"
    :okText="taskToEdit ? 'Update Task' : 'Save Task'"
    cancelText="Cancel"
    @cancel="handleCancel"
    @ok="handleSubmit"
    :confirmLoading="isSubmitting"
    destroyOnClose <!-- Add this to reset form state when modal is closed -->
  >
    <a-form ref="formRef" :model="formState" layout="vertical" name="task_form">
      <!-- ... (form items remain the same) ... -->
       <a-form-item name="title" label="Task Title" :rules="[{ required: true, message: 'Please input the title of the task!' }]">
        <a-input v-model:value="formState.title" />
      </a-form-item>
      <a-form-item name="description" label="Description">
        <a-textarea v-model:value="formState.description" :rows="4" />
      </a-form-item>
      <a-form-item name="dueDate" label="Due Date">
        <a-date-picker v-model:value="formState.dueDate" style="width: 100%;"  format="YYYY-MM-DD" />
      </a-form-item>
      <a-form-item name="projectId" label="Project" :rules="[{ required: true, message: 'Please select a project!' }]">
        <a-select v-model:value="formState.projectId" placeholder="Select a project" :disabled="!!taskToEdit && taskToEdit.isCompleted">
          <a-select-option v-for="project in projectsStore.projects" :key="project.id" :value="project.id">
            {{ project.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item name="priority" label="Priority">
         <a-radio-group v-model:value="formState.priority" :disabled="!!taskToEdit && taskToEdit.isCompleted">
           <a-radio value="low">Low</a-radio>
           <a-radio value="medium">Medium</a-radio>
           <a-radio value="high">High</a-radio>
         </a-radio-group>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, reactive, watch, defineProps, defineEmits, computed, onMounted } from 'vue';
import { useTasksStore } from '@/store/tasksStore';
import { useProjectsStore } from '@/store/projectsStore';
import dayjs from 'dayjs';
import { Modal as AModal, Form as AForm, FormItem as AFormItem, Input as AInput, Textarea as ATextarea, DatePicker as ADatePicker, Select as ASelect, SelectOption as ASelectOption, RadioGroup as ARadioGroup, Radio as ARadio, message } from 'ant-design-vue';


const props = defineProps({
  visible: { type: Boolean, required: true },
  taskToEdit: { type: Object, default: null },
  defaultProjectId: { type: String, default: 'inbox' }
});

const emit = defineEmits(['close', 'taskSaved']);

const tasksStore = useTasksStore();
const projectsStore = useProjectsStore();

const formRef = ref();
const isSubmitting = ref(false);

const getInitialFormState = () => ({
  title: '',
  description: '',
  dueDate: null,
  projectId: props.defaultProjectId || (projectsStore.projects.length > 0 ? projectsStore.projects[0].id : 'inbox'),
  priority: 'medium',
});

const formState = reactive(getInitialFormState());
const modalTitle = computed(() => (props.taskToEdit ? 'Edit Task' : 'Add New Task'));

watch(() => props.visible, (isVisible) => {
  if (isVisible) {
    if (props.taskToEdit) {
      formState.title = props.taskToEdit.title;
      formState.description = props.taskToEdit.description || '';
      formState.dueDate = props.taskToEdit.dueDate ? dayjs(props.taskToEdit.dueDate) : null;
      formState.projectId = props.taskToEdit.projectId;
      formState.priority = props.taskToEdit.priority || 'medium';
    } else {
      Object.assign(formState, getInitialFormState()); // Reset for new task
      formState.projectId = props.defaultProjectId || (projectsStore.projects.length > 0 ? projectsStore.projects[0].id : 'inbox');
    }
  }
}, { immediate: true });

onMounted(() => {
    if (projectsStore.projects.length === 0) {
        projectsStore.fetchProjects().then(() => {
            // If adding a new task and projects just loaded, update default projectId
            if (!props.taskToEdit && props.visible) {
                formState.projectId = props.defaultProjectId || (projectsStore.projects.length > 0 ? projectsStore.projects[0].id : 'inbox');
            }
        });
    }
});


const handleCancel = () => {
  // formRef.value.resetFields(); // Not needed due to destroyOnClose
  emit('close');
};

const handleSubmit = async () => {
  try {
    await formRef.value.validate();
    isSubmitting.value = true;
    const taskData = {
      ...formState,
      dueDate: formState.dueDate ? dayjs(formState.dueDate).format('YYYY-MM-DD') : null,
    };

    if (props.taskToEdit) {
      tasksStore.updateTask({ ...taskData, id: props.taskToEdit.id });
    } else {
      tasksStore.addTask(taskData);
    }

    isSubmitting.value = false;
    emit('taskSaved'); // Signal success
    emit('close'); // Close modal
  } catch (errorInfo) {
    console.log('Validation Failed:', errorInfo);
    message.error('Please correct the form errors.');
    isSubmitting.value = false;
  }
};
</script>
