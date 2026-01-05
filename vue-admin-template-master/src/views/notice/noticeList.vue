<template>
  <div>
    <!-- 搜索表单 -->
    <el-form :model="searchParm" ref="searchForm" label-width="80px" :inline="true" size="small">
      <el-form-item label="公告标题">
        <el-input v-model="searchParm.title" placeholder="请输入公告标题"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">搜索</el-button>
        <el-button icon="el-icon-close" @click="resetBtn" style="color: #ff7670">重置</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="addBtn" v-permission="['sys:noticeList:add']">新增</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableList" border stripe empty-text="暂无数据" style="width: 100%; margin-bottom: 10px">
      <el-table-column prop="title" label="公告标题"></el-table-column>
      <el-table-column prop="noticeContent" label="公告内容"></el-table-column>
      <el-table-column prop="createTime" label="发布时间">
        <template slot-scope="scope">
          {{ scope.row.createTime | formatDate }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="small" @click="editBtn(scope.row)"
                     v-permission="['sys:noticeList:edit']">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" size="small" @click="deleteBtn(scope.row)"
                     v-permission="['sys:noticeList:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @size-change="sizeChange"
      @current-change="currentChange"
      :current-page.sync="searchParm.currentPage"
      :page-sizes="[10, 20, 40, 80, 100]"
      :page-size="searchParm.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="searchParm.total"
      background
    >
    </el-pagination>

    <!-- 新增、编辑弹框 -->
    <sys-dialog
      :title="dialog.title"
      :visible="dialog.visible"
      :width="dialog.width"
      :height="dialog.height"
      @onClose="onClose"
      @onConfirm="onConfirm"
    >
      <div slot="noticeContent">
        <el-form
          :model="addModel"
          ref="addForm"
          :rules="rules"
          label-width="80px"
          :inline="false"
          size="small"
        >
          <el-form-item prop="title" label="公告标题">
            <el-input v-model="addModel.title" placeholder="请输入公告标题"></el-input>
          </el-form-item>
          <el-form-item prop="noticeContent" label="公告内容">
            <el-input type="textarea" v-model="addModel.noticeContent" placeholder="请输入公告内容" :rows="5"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </sys-dialog>
  </div>
</template>

<script>
import { getListApi, addApi, editApi, deleteApi } from '@/api/notice'
import SysDialog from '@/components/system/SysDialog.vue'
export default {
  components: {
    SysDialog
  },
  data() {
    return {
      // 搜索参数
      searchParm: {
        title: '',
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      // 表格数据
      tableList: [],
      // 弹框属性
      dialog: {
        title: '',
        visible: false,
        width: 630,
        height: 200
      },
      // 表单数据
      addModel: {
        noticeId: '',
        title: '',
        noticeContent: '',
        createTime: ''
      },
      // 表单验证规则
      rules: {
        title: [
          { required: true, message: '请输入公告标题', trigger: 'blur' }
        ],
        noticeContent: [
          { required: true, message: '请输入公告内容', trigger: 'blur' }
        ]
      },
      // 区分新增、编辑
      action: ''
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取列表
    async getList() {
      let res = await getListApi(this.searchParm)
      if (res && res.code == 200) {
        this.tableList = res.data.records
        this.searchParm.total = res.data.total
      }
    },
    // 搜索按钮
    searchBtn() {
      this.getList()
    },
    // 重置按钮
    resetBtn() {
      this.searchParm.title = ''
      this.searchParm.currentPage = 1
      this.getList()
    },
    // 新增按钮
    addBtn() {
      this.action = 'add'
      this.dialog.title = '新增公告'
      this.dialog.visible = true
      // 清空表单
      this.$resetForm('addForm', this.addModel)
    },
    // 编辑按钮
    editBtn(row) {
      this.action = 'edit'
      this.dialog.title = '编辑公告'
      this.dialog.visible = true
      // 数据回显
      this.$objCoppy(row, this.addModel)
    },
    // 删除按钮
    async deleteBtn(row) {
      const confirm = await this.$myconfirm('确定要删除该公告吗?')
      if (confirm) {
        let res = await deleteApi({ noticeId: row.noticeId })
        if (res && res.code == 200) {
          this.$message.success(res.msg)
          this.getList()
        }
      }
    },
    // 弹框关闭
    onClose() {
      this.dialog.visible = false
    },
    // 弹框确认
    onConfirm() {
      this.$refs.addForm.validate(async valid => {
        if (valid) {
          let res = null
          if (this.action == 'add') {
            res = await addApi(this.addModel)
          } else {
            res = await editApi(this.addModel)
          }
          if (res && res.code == 200) {
            this.$message.success(res.msg)
            this.dialog.visible = false
            this.getList()
          }
        }
      })
    },
    // 页数改变时触发
    currentChange(val) {
      this.searchParm.currentPage = val
      this.getList()
    },
    // 页容量改变时触发
    sizeChange(val) {
      this.searchParm.pageSize = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
