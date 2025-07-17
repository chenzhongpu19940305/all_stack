// Bilibili API 服务

// Bilibili API 基础配置
const BILIBILI_API_BASE = 'https://api.bilibili.com'
const BILIBILI_WEB_BASE = 'https://www.bilibili.com'

// 通用请求函数
async function request(url, options = {}) {
  const defaultOptions = {
    headers: {
      'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
      'Referer': 'https://www.bilibili.com',
      'Content-Type': 'application/json'
    }
  }
  
  const finalOptions = {
    ...defaultOptions,
    ...options,
    headers: {
      ...defaultOptions.headers,
      ...options.headers
    }
  }

  try {
    const response = await fetch(url, finalOptions)
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    return await response.json()
  } catch (error) {
    console.error('Bilibili API 请求失败:', error)
    throw error
  }
}

// 视频相关 API
export const videoAPI = {
  // 获取视频信息
  async getVideoInfo(bvid) {
    const url = `${BILIBILI_API_BASE}/x/web-interface/view?bvid=${bvid}`
    return await request(url)
  },

  // 获取视频播放地址
  async getVideoPlayUrl(bvid, cid, quality = 80) {
    const url = `${BILIBILI_API_BASE}/x/player/playurl?bvid=${bvid}&cid=${cid}&qn=${quality}&fnval=16`
    return await request(url)
  },

  // 获取视频评论
  async getVideoComments(bvid, page = 1, size = 20) {
    const url = `${BILIBILI_API_BASE}/x/v2/reply?type=1&oid=${bvid}&pn=${page}&ps=${size}&sort=2`
    return await request(url)
  },

  // 获取视频弹幕
  async getVideoDanmaku(cid) {
    const url = `${BILIBILI_API_BASE}/x/v1/dm/list.so?oid=${cid}`
    return await request(url)
  },

  // 获取视频分P信息
  async getVideoParts(bvid) {
    const url = `${BILIBILI_API_BASE}/x/player/pagelist?bvid=${bvid}`
    return await request(url)
  }
}

// 搜索相关 API
export const searchAPI = {
  // 搜索视频
  async searchVideos(keyword, page = 1, size = 20) {
    const url = `${BILIBILI_API_BASE}/x/web-interface/search/type?search_type=video&keyword=${encodeURIComponent(keyword)}&page=${page}&pagesize=${size}`
    return await request(url)
  },

  // 搜索用户
  async searchUsers(keyword, page = 1, size = 20) {
    const url = `${BILIBILI_API_BASE}/x/web-interface/search/type?search_type=bili_user&keyword=${encodeURIComponent(keyword)}&page=${page}&pagesize=${size}`
    return await request(url)
  },

  // 综合搜索
  async searchAll(keyword, page = 1, size = 20) {
    const url = `${BILIBILI_API_BASE}/x/web-interface/search/type?search_type=all&keyword=${encodeURIComponent(keyword)}&page=${page}&pagesize=${size}`
    return await request(url)
  }
}

// 用户相关 API
export const userAPI = {
  // 获取用户信息
  async getUserInfo(uid) {
    const url = `${BILIBILI_API_BASE}/x/space/acc/info?mid=${uid}`
    return await request(url)
  },

  // 获取用户视频列表
  async getUserVideos(uid, page = 1, size = 20) {
    const url = `${BILIBILI_API_BASE}/x/space/arc/search?mid=${uid}&pn=${page}&ps=${size}&jsonp=jsonp`
    return await request(url)
  },

  // 获取用户关注列表
  async getUserFollowings(uid, page = 1, size = 20) {
    const url = `${BILIBILI_API_BASE}/x/relation/followings?vmid=${uid}&pn=${page}&ps=${size}&order=desc&jsonp=jsonp`
    return await request(url)
  },

  // 获取用户粉丝列表
  async getUserFollowers(uid, page = 1, size = 20) {
    const url = `${BILIBILI_API_BASE}/x/relation/followers?vmid=${uid}&pn=${page}&ps=${size}&order=desc&jsonp=jsonp`
    return await request(url)
  }
}

// 分区相关 API
export const categoryAPI = {
  // 获取分区列表
  async getCategories() {
    const url = `${BILIBILI_API_BASE}/x/web-interface/web/channel/category/list`
    return await request(url)
  },

  // 获取分区视频
  async getCategoryVideos(tid, page = 1, size = 20) {
    const url = `${BILIBILI_API_BASE}/x/web-interface/web/channel/multiple/list?channel_id=${tid}&sort_type=hot&page_size=${size}&page=${page}`
    return await request(url)
  }
}

// 热门推荐 API
export const popularAPI = {
  // 获取热门视频
  async getPopularVideos(page = 1, size = 20) {
    const url = `${BILIBILI_API_BASE}/x/web-interface/popular?ps=${size}&pn=${page}`
    return await request(url)
  },

  // 获取推荐视频
  async getRecommendVideos(page = 1, size = 20) {
    const url = `${BILIBILI_API_BASE}/x/web-interface/dynamic/region?ps=${size}&rid=1&pn=${page}`
    return await request(url)
  },

  // 获取排行榜
  async getRankingList(tid = 0, day = 3) {
    const url = `${BILIBILI_API_BASE}/x/web-interface/ranking/region?rid=${tid}&day=${day}`
    return await request(url)
  }
}

// 直播相关 API
export const liveAPI = {
  // 获取直播间信息
  async getLiveRoomInfo(roomId) {
    const url = `${BILIBILI_API_BASE}/x/room/v1/Room/get_info?room_id=${roomId}`
    return await request(url)
  },

  // 获取直播弹幕
  async getLiveDanmaku(roomId) {
    const url = `${BILIBILI_API_BASE}/x/room/v1/Danmaku/getSeg?room_id=${roomId}`
    return await request(url)
  }
}

// 工具函数
export const utils = {
  // 从 URL 中提取 BV 号
  extractBvid(url) {
    const bvidMatch = url.match(/BV[a-zA-Z0-9]+/)
    return bvidMatch ? bvidMatch[0] : null
  },

  // 从 URL 中提取 UID
  extractUid(url) {
    const uidMatch = url.match(/space\.bilibili\.com\/(\d+)/)
    return uidMatch ? uidMatch[1] : null
  },

  // 格式化播放量
  formatPlayCount(count) {
    if (count >= 10000) {
      return (count / 10000).toFixed(1) + '万'
    }
    return count.toString()
  },

  // 格式化时长
  formatDuration(seconds) {
    const hours = Math.floor(seconds / 3600)
    const minutes = Math.floor((seconds % 3600) / 60)
    const secs = seconds % 60
    
    if (hours > 0) {
      return `${hours}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
    }
    return `${minutes}:${secs.toString().padStart(2, '0')}`
  }
}

// 默认导出所有 API
export default {
  video: videoAPI,
  search: searchAPI,
  user: userAPI,
  category: categoryAPI,
  popular: popularAPI,
  live: liveAPI,
  utils
}
