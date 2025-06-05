// src/store/appStore.js
import { defineStore } from 'pinia';

export const useAppStore = defineStore('app', {
  state: () => ({
    selectedKey: 'inbox', // Can be a projectId or a smart list key e.g., 'today'
    selectedType: 'project', // 'project' or 'smartlist'
  }),
  actions: {
    setSelectedKey(key, type = 'project') {
      this.selectedKey = key;
      this.selectedType = type;
      console.log(`AppStore: Selected ${type} - ${key}`);
    },
  },
  getters: {
    currentSelectedProjectId: (state) => {
      return state.selectedType === 'project' ? state.selectedKey : null;
    },
    currentSelectedSmartList: (state) => {
      return state.selectedType === 'smartlist' ? state.selectedKey : null;
    }
  }
});
