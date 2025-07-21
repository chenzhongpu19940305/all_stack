<template>
  <div class="search-container">
    <div class="search-input-wrapper">
      <div class="search-icon">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M21 21L16.514 16.506L21 21ZM19 10.5C19 15.194 15.194 19 10.5 19C5.806 19 2 15.194 2 10.5C2 5.806 5.806 2 10.5 2C15.194 2 19 5.806 19 10.5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
      <input
        ref="searchInput"
        v-model="searchValue"
        type="text"
        class="search-input"
        :placeholder="placeholder"
        @input="handleInput"
        @keyup.enter="handleSearch"
        @focus="handleFocus"
        @blur="handleBlur"
      />
      <button
        v-if="showClearButton"
        class="clear-button"
        @click="clearSearch"
        type="button"
      >
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M18 6L6 18M6 6L18 18" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'

// Props
const props = defineProps({
  placeholder: {
    type: String,
    default: '搜索...'
  },
  modelValue: {
    type: String,
    default: ''
  },
  debounceTime: {
    type: Number,
    default: 300
  },
  autoSearch: {
    type: Boolean,
    default: true
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'search', 'input', 'focus', 'blur', 'clear'])

// Reactive data
const searchValue = ref(props.modelValue)
const searchInput = ref(null)
const isFocused = ref(false)
const debounceTimer = ref(null)

// Computed
const showClearButton = computed(() => {
  return searchValue.value.length > 0 && isFocused.value
})

// Methods
const handleInput = () => {
  emit('update:modelValue', searchValue.value)
  emit('input', searchValue.value)
  
  if (props.autoSearch) {
    clearTimeout(debounceTimer.value)
    debounceTimer.value = setTimeout(() => {
      handleSearch()
    }, props.debounceTime)
  }
}

const handleSearch = () => {
  emit('search', searchValue.value)
}

const handleFocus = () => {
  isFocused.value = true
  emit('focus')
}

const handleBlur = () => {
  isFocused.value = false
  emit('blur')
}

const clearSearch = () => {
  searchValue.value = ''
  emit('update:modelValue', '')
  emit('clear')
  emit('search', '')
  nextTick(() => {
    searchInput.value?.focus()
  })
}

const focus = () => {
  searchInput.value?.focus()
}

const blur = () => {
  searchInput.value?.blur()
}

// Watch for external modelValue changes
watch(() => props.modelValue, (newValue) => {
  searchValue.value = newValue
})

// Expose methods
defineExpose({
  focus,
  blur,
  clearSearch
})
</script>

<style scoped>
.search-container {
  width: 100%;
}

.search-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
  background: #ffffff;
  border: 1px solid #e1e5e9;
  border-radius: 8px;
  transition: all 0.2s ease;
  overflow: hidden;
}

.search-input-wrapper:hover {
  border-color: #c1c5c9;
}

.search-input-wrapper:focus-within {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.search-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  color: #6b7280;
  flex-shrink: 0;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  padding: 8px 12px;
  font-size: 14px;
  background: transparent;
  color: #374151;
}

.search-input::placeholder {
  color: #9ca3af;
}

.clear-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border: none;
  background: transparent;
  color: #6b7280;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.2s ease;
  margin-right: 4px;
}

.clear-button:hover {
  background: #f3f4f6;
  color: #374151;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-input-wrapper {
    border-radius: 6px;
  }
  
  .search-icon {
    width: 36px;
    height: 36px;
  }
  
  .search-input {
    font-size: 16px; /* 移动端防止缩放 */
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .search-input-wrapper {
    background: #1f2937;
    border-color: #374151;
  }
  
  .search-input-wrapper:hover {
    border-color: #4b5563;
  }
  
  .search-input-wrapper:focus-within {
    border-color: #3b82f6;
  }
  
  .search-input {
    color: #f9fafb;
  }
  
  .search-input::placeholder {
    color: #6b7280;
  }
  
  .clear-button:hover {
    background: #374151;
    color: #f9fafb;
  }
}
</style> 