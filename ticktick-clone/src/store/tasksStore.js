// src/store/tasksStore.js
import { defineStore } from 'pinia';
import { fetchData } from '@/services/dataService';
import dayjs from 'dayjs'; // Import dayjs for date comparisons

export const useTasksStore = defineStore('tasks', {
  state: () => ({
    tasks: [],
    isLoading: false,
    error: null,
  }),
  actions: {
    async fetchTasks() {
      this.isLoading = true;
      this.error = null;
      try {
        const data = await fetchData(); // fetchData gets both projects and tasks
        this.tasks = data.tasks.map(task => ({
          ...task,
          isCompleted: !!task.isCompleted, // Ensure boolean
          dueDate: task.dueDate || null // Ensure null if empty for date pickers
        }));
      } catch (e) {
        this.error = e;
        console.error('Failed to fetch tasks:', e);
      } finally {
        this.isLoading = false;
      }
    },
    addTask(task) {
      if (task && task.title) {
        const newTask = {
          id: `task-${Date.now()}`,
          description: '',
          isCompleted: false,
          priority: 'medium',
          ...task, // Spread incoming task to override defaults
          dueDate: task.dueDate || null
        };
        this.tasks.push(newTask);
      }
    },
    updateTaskCompletion({ id, isCompleted }) {
      const task = this.tasks.find(t => t.id === id);
      if (task) {
        task.isCompleted = isCompleted;
      }
    },
    updateTask(updatedTask) {
      const index = this.tasks.findIndex(t => t.id === updatedTask.id);
      if (index !== -1) {
        this.tasks[index] = { ...this.tasks[index], ...updatedTask };
      } else {
        console.warn('Task not found for update:', updatedTask.id);
      }
    },
    deleteTask(taskId) {
      this.tasks = this.tasks.filter(t => t.id !== taskId);
    }
  },
  getters: {
    getTasksByProjectId: (state) => (projectId) => {
      return state.tasks.filter(task => task.projectId === projectId);
    },
    getTaskById: (state) => (id) => {
      return state.tasks.find(task => task.id === id);
    },
    // Smart List Getters
    getTasksDueToday: (state) => {
      const today = dayjs().format('YYYY-MM-DD');
      return state.tasks.filter(task => task.dueDate === today && !task.isCompleted);
    },
    getTasksDueNext7Days: (state) => {
      const today = dayjs().startOf('day');
      const sevenDaysLater = today.add(7, 'day').endOf('day'); // Include tasks due on the 7th day
      return state.tasks.filter(task => {
        if (!task.dueDate || task.isCompleted) return false;
        const dueDate = dayjs(task.dueDate);
        // Tasks due from today up to 7 days from now
        return dueDate.isSameOrAfter(today, 'day') && dueDate.isSameOrBefore(sevenDaysLater, 'day');
      });
    },
    // Getter for all tasks (useful for an "All Tasks" view if ever needed)
    getAllTasks: (state) => {
        return state.tasks;
    }
  }
});
