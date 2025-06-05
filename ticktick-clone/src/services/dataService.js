// src/services/dataService.js
export const fetchData = async () => {
  try {
    // In Vite, files in `public` are served at the root.
    const response = await fetch('/data/initialData.json');
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Could not fetch initial data:", error);
    // Return empty structure or handle error as appropriate for your app
    return { projects: [], tasks: [] };
  }
};
