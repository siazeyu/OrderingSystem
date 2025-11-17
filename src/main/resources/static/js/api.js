/**
 * API工具类 - 统一管理所有后端请求
 */

/**
 * 加载提示工具类
 */
class LoadingManager {
    constructor() {
        this.loadingCount = 0;
        this.loadingElement = null;
        this.init();
    }

    init() {
        // 创建加载提示元素
        this.loadingElement = document.createElement('div');
        this.loadingElement.id = 'global-loading';
        this.loadingElement.innerHTML = `
            <div class="loading-overlay">
                <div class="loading-spinner">
                    <div class="spinner"></div>
                    <div class="loading-text">加载中...</div>
                </div>
            </div>
        `;
        
        // 添加样式
        const style = document.createElement('style');
        style.textContent = `
            #global-loading {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                z-index: 9999;
                display: none;
            }
            
            .loading-overlay {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5);
                display: flex;
                justify-content: center;
                align-items: center;
            }
            
            .loading-spinner {
                background: white;
                padding: 20px 30px;
                border-radius: 8px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
                display: flex;
                flex-direction: column;
                align-items: center;
                gap: 15px;
            }
            
            .spinner {
                width: 32px;
                height: 32px;
                border: 3px solid #f3f3f3;
                border-top: 3px solid #007bff;
                border-radius: 50%;
                animation: spin 1s linear infinite;
            }
            
            .loading-text {
                font-size: 14px;
                color: #333;
                font-weight: 500;
            }
            
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            
            .btn-loading {
                position: relative;
                pointer-events: none;
                opacity: 0.7;
            }
            
            .btn-loading::after {
                content: '';
                position: absolute;
                top: 50%;
                left: 50%;
                width: 16px;
                height: 16px;
                margin: -8px 0 0 -8px;
                border: 2px solid transparent;
                border-top: 2px solid currentColor;
                border-radius: 50%;
                animation: spin 0.8s linear infinite;
            }
            
            .submitting {
                position: relative;
                pointer-events: none;
                opacity: 0.7;
            }
            
            .submitting::after {
                content: '';
                position: absolute;
                top: 50%;
                left: 50%;
                width: 12px;
                height: 12px;
                margin: -6px 0 0 -6px;
                border: 2px solid transparent;
                border-top: 2px solid currentColor;
                border-radius: 50%;
                animation: spin 0.8s linear infinite;
            }
            
            .form-submitting {
                opacity: 0.6;
                cursor: not-allowed;
            }
        `;
        
        document.head.appendChild(style);
        document.body.appendChild(this.loadingElement);
    }

    show(message = '加载中...') {
        this.loadingCount++;
        
        if (this.loadingCount === 1) {
            const loadingText = this.loadingElement.querySelector('.loading-text');
            loadingText.textContent = message;
            this.loadingElement.style.display = 'block';
        }
    }

    hide() {
        this.loadingCount = Math.max(0, this.loadingCount - 1);
        
        if (this.loadingCount === 0) {
            this.loadingElement.style.display = 'none';
        }
    }

    // 按钮加载状态
    setButtonLoading(button, loading = true, originalText = '') {
        if (!button) return;
        
        if (loading) {
            button.dataset.originalText = originalText || button.textContent;
            button.classList.add('btn-loading');
            button.disabled = true;
        } else {
            button.classList.remove('btn-loading');
            button.disabled = false;
            button.textContent = button.dataset.originalText || originalText;
        }
    }
}

/**
 * 防重复提交工具类
 */
class SubmitManager {
    constructor() {
        this.submittingButtons = new Set(); // 正在提交的按钮集合
        this.submittingForms = new Set();   // 正在提交的表单集合
    }

    /**
     * 检查按钮是否可以提交
     * @param {HTMLElement} button - 按钮元素
     * @returns {boolean} 是否可以提交
     */
    canSubmit(button) {
        if (!button) return false;
        return !this.submittingButtons.has(button);
    }

    /**
     * 设置按钮提交状态
     * @param {HTMLElement} button - 按钮元素
     * @param {boolean} submitting - 是否正在提交
     * @param {string} loadingText - 加载时显示的文本
     */
    setButtonSubmitting(button, submitting = true, loadingText = '提交中...') {
        if (!button) return;

        if (submitting) {
            // 标记为正在提交
            this.submittingButtons.add(button);
            button.disabled = true;
            button.dataset.originalText = button.textContent;
            button.textContent = loadingText;
            button.classList.add('submitting');
        } else {
            // 移除提交状态
            this.submittingButtons.delete(button);
            button.disabled = false;
            button.textContent = button.dataset.originalText || button.textContent;
            button.classList.remove('submitting');
        }
    }

    /**
     * 检查表单是否可以提交
     * @param {HTMLElement} form - 表单元素
     * @returns {boolean} 是否可以提交
     */
    canSubmitForm(form) {
        if (!form) return false;
        return !this.submittingForms.has(form);
    }

    /**
     * 设置表单提交状态
     * @param {HTMLElement} form - 表单元素
     * @param {boolean} submitting - 是否正在提交
     */
    setFormSubmitting(form, submitting = true) {
        if (!form) return;

        if (submitting) {
            this.submittingForms.add(form);
            
            // 禁用表单内所有按钮和输入框
            const buttons = form.querySelectorAll('button, input[type="submit"], input[type="button"]');
            const inputs = form.querySelectorAll('input:not([type="hidden"]), select, textarea');
            
            buttons.forEach(btn => {
                btn.disabled = true;
                btn.classList.add('form-submitting');
            });
            
            inputs.forEach(input => {
                if (input.type !== 'hidden') {
                    input.disabled = true;
                    input.classList.add('form-submitting');
                }
            });
        } else {
            this.submittingForms.delete(form);
            
            // 恢复表单内所有元素状态
            const elements = form.querySelectorAll('.form-submitting');
            elements.forEach(element => {
                element.disabled = false;
                element.classList.remove('form-submitting');
            });
        }
    }

    /**
     * 防重复提交包装器
     * @param {Function} asyncFunction - 异步函数
     * @param {HTMLElement} element - 按钮或表单元素
     * @param {string} loadingText - 加载文本
     * @returns {Function} 包装后的函数
     */
    preventDuplicate(asyncFunction, element, loadingText = '处理中...') {
        return async (...args) => {
            // 检查是否可以提交
            if (element.tagName === 'FORM') {
                if (!this.canSubmitForm(element)) {
                    return;
                }
                this.setFormSubmitting(element, true, loadingText);
            } else {
                if (!this.canSubmit(element)) {
                    return;
                }
                this.setButtonSubmitting(element, true, loadingText);
            }

            try {
                return await asyncFunction(...args);
            } finally {
                // 恢复状态
                if (element.tagName === 'FORM') {
                    this.setFormSubmitting(element, false);
                } else {
                    this.setButtonSubmitting(element, false);
                }
            }
        };
    }
}


// 创建全局加载管理器实例，等待DOM加载完成
let loadingManager;
let submitManager;

// 等待DOM加载完成后再初始化
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        loadingManager = new LoadingManager();
        submitManager = new SubmitManager();
    });
} else {
    // DOM已经加载完成
    loadingManager = new LoadingManager();
    submitManager = new SubmitManager();
}

class ApiClient {
    constructor() {
        this.baseURL = '';
    }

    /**
     * 通用请求方法
     */
    async request(url, options = {}) {
        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };

        // 显示加载提示
        const loadingMessage = options.loadingMessage || '请求中...';
        const showLoading = options.showLoading !== false; // 默认显示加载
        
        if (showLoading) {
            loadingManager.show(loadingMessage);
        }

        try {
            const response = await fetch(this.baseURL + url, config);
            const result = await response.json();
            
            // 统一处理响应格式，添加success字段
            if (result.code === 200) {
                result.success = true;
            } else {
                result.success = false;
            }
            
            return result;
        } catch (error) {
            console.error('API请求失败:', error);
            throw error;
        } finally {
            // 隐藏加载提示
            if (showLoading) {
                loadingManager.hide();
            }
        }
    }

    /**
     * GET请求
     */
    async get(url, params = {}, options = {}) {
        const queryString = new URLSearchParams(params).toString();
        const fullUrl = queryString ? `${url}?${queryString}` : url;
        return this.request(fullUrl, { method: 'GET', ...options });
    }

    /**
     * POST请求 - JSON格式
     */
    async postJson(url, data = {}) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    /**
     * POST请求 - 表单格式
     */
    async postForm(url, data = {}, options = {}) {
        const formData = new URLSearchParams();
        Object.keys(data).forEach(key => {
            formData.append(key, data[key]);
        });

        return this.request(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData.toString(),
            ...options
        });
    }

    /**
     * PUT请求
     */
    async put(url, data = {}) {
        return this.request(url, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }

    /**
     * DELETE请求
     */
    async delete(url, params = {}, options = {}) {
        const queryString = new URLSearchParams(params).toString();
        const fullUrl = queryString ? `${url}?${queryString}` : url;
        return this.request(fullUrl, { method: 'DELETE', ...options });
    }

    // ==================== 用户相关API ====================
    
    /**
     * 用户登录
     */
    async login(username, password) {
        return this.postForm('/user/login', { username, password }, {
            loadingMessage: '登录中...'
        });
    }

    /**
     * 用户注册
     */
    async register(username, phone, password) {
        return this.postForm('/user/register', { username, phone, password }, {
            loadingMessage: '注册中...'
        });
    }

    /**
     * 检查用户名是否存在
     */
    async checkUsername(username) {
        return this.get('/user/check/username', { username });
    }

    /**
     * 检查手机号是否存在
     */
    async checkPhone(phone) {
        return this.get('/user/check/phone', { phone });
    }

    /**
     * 更新用户信息
     */
    async updateUserInfo(userId, userInfo) {
        return this.postForm('/user/update', { userId, ...userInfo });
    }

    // ==================== 商品分类相关API ====================
    
    /**
     * 获取所有分类
     */
    async getCategories() {
        return this.get('/category/list', {}, {
            loadingMessage: '加载分类...'
        });
    }

    /**
     * 根据分类获取商品
     */
    async getProductsByCategory(categoryId) {
        const loadingMessage = categoryId ? '加载商品...' : '加载全部商品...';
        return categoryId ? 
            this.get(`/product/category/${categoryId}`, {}, { loadingMessage }) : 
            this.get('/product/list', {}, { loadingMessage });
    }

    // ==================== 购物车相关API ====================
    
    /**
     * 获取用户购物车
     */
    async getCartItems(userId) {
        return this.get(`/cart/list/${userId}`);
    }

    /**
     * 添加商品到购物车
     */
    async addToCart(userId, productId, quantity = 1) {
        return this.postForm('/cart/add', { userId, productId, quantity }, {
            loadingMessage: '添加到购物车...'
        });
    }

    /**
     * 更新购物车商品数量
     */
    async updateCartQuantity(userId, productId, quantity) {
        return this.postForm('/cart/update', { userId, productId, quantity }, {
            loadingMessage: '更新数量...'
        });
    }

    /**
     * 从购物车移除商品
     */
    async removeFromCart(userId, productId) {
        return this.delete('/cart/remove', { userId, productId }, {
            loadingMessage: '移除商品...'
        });
    }

    /**
     * 清空购物车
     */
    async clearCart(userId) {
        return this.delete(`/cart/clear/${userId}`);
    }

    /**
     * 获取购物车汇总信息
     */
    async getCartSummary(userId) {
        return this.get(`/cart/total/${userId}`);
    }

    /**
     * 迁移游客购物车数据
     */
    async migrateGuestCart(userId, cartData) {
        return this.postJson(`/cart/migrate?userId=${userId}`, cartData);
    }

    // ==================== 订单相关API ====================
    
    /**
     * 创建订单（从购物车）
     */
    async createOrderFromCart(userId, remark = '', deliveryAddress = '', contactName = '', contactPhone = '') {
        return this.postForm('/order/create', { 
            userId, 
            remark, 
            deliveryAddress, 
            contactName, 
            contactPhone 
        }, {
            loadingMessage: '正在创建订单...'
        });
    }

    /**
     * 支付订单
     */
    async payOrder(orderId) {
        return this.request(`/order/${orderId}/pay`, {
            method: 'POST'
        });
    }

    /**
     * 获取用户订单列表
     */
    async getUserOrders(userId) {
        return this.get(`/order/user/${userId}`);
    }

    /**
     * 获取订单详情
     */
    async getOrderDetail(orderId) {
        return this.get(`/order/detail/${orderId}`);
    }

    /**
     * 取消订单
     */
    async cancelOrder(orderId) {
        return this.put(`/order/${orderId}/cancel`);
    }

    /**
     * 完成订单（确认收货）
     */
    async completeOrder(orderId) {
        return this.put(`/order/${orderId}/complete`);
    }

    /**
     * 重新下单
     */
    async reorder(orderId) {
        return this.postForm('/order/reorder', { orderId });
    }

    // ==================== 管理员相关API ====================
    
    /**
     * 获取所有商品列表（管理员）
     */
    async getAllProducts() {
        return this.get('/product/list');
    }

    /**
     * 获取所有分类列表（管理员）
     */
    async getAllCategories() {
        return this.get('/category/list');
    }

    /**
     * 获取所有订单列表（管理员）
     */
    async getAllOrders() {
        return this.get('/order/list');
    }

    /**
     * 更新订单状态（管理员）
     */
    async updateOrderStatus(orderId, status) {
        return this.request(`/order/${orderId}/status?status=${encodeURIComponent(status)}`, {
            method: 'PUT'
        });
    }

    /**
     * 添加商品（管理员）
     */
    async addProduct(productData) {
        // 转换状态：字符串转整数，价格转数字
        const data = {
            ...productData,
            status: productData.status === 'available' ? 1 : 0,
            price: parseFloat(productData.price),
            originalPrice: productData.originalPrice ? parseFloat(productData.originalPrice) : null,
            stock: parseInt(productData.stock),
            categoryId: parseInt(productData.categoryId)
        };
        return this.postJson('/product', data);
    }

    /**
     * 更新商品（管理员）
     */
    async updateProduct(productId, productData) {
        // 转换状态：字符串转整数，价格转数字
        const data = {
            ...productData,
            status: productData.status === 'available' ? 1 : 0,
            price: parseFloat(productData.price),
            originalPrice: productData.originalPrice ? parseFloat(productData.originalPrice) : null,
            stock: parseInt(productData.stock),
            categoryId: parseInt(productData.categoryId)
        };
        return this.put(`/product/${productId}`, data);
    }

    /**
     * 删除商品（管理员）
     */
    async deleteProduct(productId) {
        return this.delete(`/product/${productId}`);
    }

    /**
     * 添加分类（管理员）
     */
    async addCategory(categoryData) {
        // 转换状态：字符串转整数
        const data = {
            ...categoryData,
            status: categoryData.status === 'available' ? 1 : 0
        };
        return this.postJson('/category', data);
    }

    /**
     * 更新分类（管理员）
     */
    async updateCategory(categoryId, categoryData) {
        // 转换状态：字符串转整数
        const data = {
            ...categoryData,
            status: categoryData.status === 'available' ? 1 : 0
        };
        return this.put(`/category/${categoryId}`, data);
    }

    /**
     * 删除分类（管理员）
     */
    async deleteCategory(categoryId) {
        return this.delete(`/category/${categoryId}`);
    }

    // ==================== 钱包相关API ====================
    
    /**
     * 获取用户钱包信息
     */
    async getUserWallet(userId) {
        return this.get(`/wallet/user/${userId}`);
    }

    /**
     * 获取用户余额
     */
    async getBalance(userId) {
        return this.get(`/wallet/balance/${userId}`);
    }

    /**
     * 钱包充值
     */
    async recharge(userId, amount) {
        return this.postJson('/wallet/recharge', { userId, amount });
    }

    /**
     * 快捷充值（预设金额）
     */
    async quickRecharge(userId, amount) {
        return this.postJson('/wallet/quick-recharge', { userId, amount });
    }

    /**
     * 检查余额是否充足
     */
    async checkBalance(userId, amount) {
        return this.postJson('/wallet/check-balance', { userId, amount });
    }

    /**
     * 钱包消费
     */
    async consume(userId, amount) {
        return this.postJson('/wallet/consume', { userId, amount });
    }

    /**
     * 冻结余额
     */
    async freezeBalance(userId, amount) {
        return this.postJson('/wallet/freeze', { userId, amount });
    }

    /**
     * 解冻余额
     */
    async unfreezeBalance(userId, amount) {
        return this.postJson('/wallet/unfreeze', { userId, amount });
    }

    // ==================== 用户地址管理API ====================
    
    /**
     * 获取用户的所有收货地址
     */
    async getUserAddresses(userId) {
        return this.get(`/users/${userId}/addresses`);
    }

    /**
     * 获取用户的默认收货地址
     */
    async getDefaultAddress(userId) {
        return this.get(`/users/${userId}/addresses/default`);
    }

    /**
     * 获取单个地址详情
     */
    async getUserAddress(userId, addressId) {
        return this.get(`/users/${userId}/addresses/${addressId}`);
    }

    /**
     * 添加收货地址
     */
    async addUserAddress(userId, addressData) {
        return this.postJson(`/users/${userId}/addresses`, addressData);
    }

    /**
     * 更新收货地址
     */
    async updateUserAddress(userId, addressId, addressData) {
        return this.put(`/users/${userId}/addresses/${addressId}`, addressData);
    }

    /**
     * 部分更新收货地址
     */
    async patchUserAddress(userId, addressId, updates) {
        return this.request(`/users/${userId}/addresses/${addressId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updates)
        });
    }

    /**
     * 删除收货地址
     */
    async deleteUserAddress(userId, addressId) {
        return this.delete(`/users/${userId}/addresses/${addressId}`);
    }

    /**
     * 设置默认地址
     */
    async setDefaultAddress(userId, addressId) {
        return this.request(`/users/${userId}/addresses/${addressId}/default`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            }
        });
    }

    /**
     * 批量删除地址
     */
    async batchDeleteAddresses(userId, addressIds) {
        return this.delete(`/users/${userId}/addresses/batch`, {}, { addressIds });
    }

    // ==================== 用户信息管理API ====================
    
    /**
     * 获取用户信息
     */
    async getUser(userId) {
        return this.get(`/user/${userId}`);
    }

    /**
     * 更新用户信息
     */
    async updateUser(userId, userData) {
        return this.postForm('/user/update', { userId, ...userData });
    }

    /**
     * 修改密码
     */
    async changePassword(userId, passwordData) {
        return this.postForm('/user/change-password', { userId, ...passwordData });
    }
}

// 创建分组API对象
const api = new ApiClient();

// 创建分组对象
api.user = {
    login: (username, password) => api.login(username, password),
    register: (username, phone, password) => api.register(username, phone, password),
    checkUsername: (username) => api.checkUsername(username),
    checkPhone: (phone) => api.checkPhone(phone),
    getUser: (userId) => api.getUser(userId),
    updateUser: (userId, userData) => api.updateUser(userId, userData),
    changePassword: (userId, passwordData) => api.changePassword(userId, passwordData)
};

api.product = {
    getCategories: () => api.getCategories(),
    getProductsByCategory: (categoryId) => api.getProductsByCategory(categoryId),
    getAllProducts: () => api.getAllProducts(),
    addProduct: (productData) => api.addProduct(productData),
    updateProduct: (productId, productData) => api.updateProduct(productId, productData),
    deleteProduct: (productId) => api.deleteProduct(productId)
};

api.category = {
    getCategories: () => api.getCategories(),
    addCategory: (categoryData) => api.addCategory(categoryData),
    updateCategory: (categoryId, categoryData) => api.updateCategory(categoryId, categoryData),
    deleteCategory: (categoryId) => api.deleteCategory(categoryId)
};

api.cart = {
    getCartItems: (userId) => api.getCartItems(userId),
    addToCart: (userId, productId, quantity) => api.addToCart(userId, productId, quantity),
    updateCartQuantity: (userId, productId, quantity) => api.updateCartQuantity(userId, productId, quantity),
    removeFromCart: (userId, productId) => api.removeFromCart(userId, productId),
    clearCart: (userId) => api.clearCart(userId),
    getCartSummary: (userId) => api.getCartSummary(userId),
    migrateGuestCart: (userId, cartData) => api.migrateGuestCart(userId, cartData)
};

api.order = {
    createOrderFromCart: (userId, remark, deliveryAddress, contactName, contactPhone) => 
        api.createOrderFromCart(userId, remark, deliveryAddress, contactName, contactPhone),
    payOrder: (orderId) => api.payOrder(orderId),
    getUserOrders: (userId) => api.getUserOrders(userId),
    getOrderDetail: (orderId) => api.getOrderDetail(orderId),
    cancelOrder: (orderId) => api.cancelOrder(orderId),
    completeOrder: (orderId) => api.completeOrder(orderId),
    reorder: (orderId) => api.reorder(orderId),
    getAllOrders: () => api.getAllOrders(),
    updateOrderStatus: (orderId, status) => api.updateOrderStatus(orderId, status)
};

api.wallet = {
    getUserWallet: (userId) => api.getUserWallet(userId),
    getBalance: (userId) => api.getBalance(userId),
    recharge: (userId, amount) => api.recharge(userId, amount),
    quickRecharge: (userId, amount) => api.quickRecharge(userId, amount),
    checkBalance: (userId, amount) => api.checkBalance(userId, amount),
    consume: (userId, amount) => api.consume(userId, amount),
    freezeBalance: (userId, amount) => api.freezeBalance(userId, amount),
    unfreezeBalance: (userId, amount) => api.unfreezeBalance(userId, amount)
};

api.userAddress = {
    getUserAddresses: (userId) => api.getUserAddresses(userId),
    getDefaultAddress: (userId) => api.getDefaultAddress(userId),
    getUserAddress: (userId, addressId) => api.getUserAddress(userId, addressId),
    addUserAddress: (userId, addressData) => api.addUserAddress(userId, addressData),
    updateUserAddress: (userId, addressId, addressData) => api.updateUserAddress(userId, addressId, addressData),
    patchUserAddress: (userId, addressId, updates) => api.patchUserAddress(userId, addressId, updates),
    deleteUserAddress: (userId, addressId) => api.deleteUserAddress(userId, addressId),
    setDefaultAddress: (userId, addressId) => api.setDefaultAddress(userId, addressId),
    batchDeleteAddresses: (userId, addressIds) => api.batchDeleteAddresses(userId, addressIds)
};