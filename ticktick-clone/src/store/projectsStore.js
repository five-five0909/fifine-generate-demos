// src/store/projectsStore.js
import { defineStore } from 'pinia';
import { fetchData } from '@/services/dataService';

export const useProjectsStore = defineStore('projects', {
  state: () => ({
    projects: [],
    isLoading: false,
    error: null,
  }),
  actions: {
    async fetchProjects() {
      this.isLoading = true;
      this.error = null;
      try {
        const data = await fetchData(); // fetchData gets both projects and tasks
        this.projects = data.projects;
      } catch (e) {
        this.error = e;
        console.error('Failed to fetch projects:', e);
      } finally {
        this.isLoading = false;
      }
    },
    addProject(project) {
      if (project && project.name) {
        const newProjectData = {
          id: project.id || `project-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`, // More robust ID
          name: project.name,
          // Add other default project properties if any
        };
        // Check for duplicate names or IDs if necessary
        if (!this.projects.find(p => p.name.toLowerCase() === newProjectData.name.toLowerCase())) {
          this.projects.push(newProjectData);
        } else {
          console.warn('Project with this name already exists.');
          // Optionally: throw error or notify user
        }
      }
    }
    // More actions like updateProject, deleteProject will be added later
  },
  getters: {
    getProjectById: (state) => (id) => {
      return state.projects.find(project => project.id === id);
    }
  }
});
