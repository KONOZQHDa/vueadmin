<template>
    <div>
<!--        新增按钮，用这种行内表单中自带的按钮是因为使用它就不用自己再去调按钮的样式和间距之类了，本身独占一行-->
        <el-form :inline="true" class="demo-form-inline">
            <el-form-item>
                <el-button type="primary" @click="dialogVisible = true">新增</el-button>
            </el-form-item>
        </el-form>
<!--        菜单管理的表格-->
        <el-table
            :data="tableData"
            style="width: 100%;margin-bottom: 20px;"
            row-key="id"
            border
            stripe

            default-expand-all
            :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
            <el-table-column
                prop="name"
                label="名称"
                sortable
                align="center"
                width="180">
            </el-table-column>
            <el-table-column
                prop="permit"
                label="权限编码"
                align="center"
                width="180">
            </el-table-column>
            <el-table-column
                prop="type"
                width="80px"
                align="center"
                label="类型">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.type === 0" type="info">目录</el-tag>
                    <el-tag v-if="scope.row.type === 1">菜单</el-tag>
                    <el-tag v-if="scope.row.type === 2" type="success">按钮</el-tag>

                </template>
            </el-table-column>
            <el-table-column
                prop="url"
                label="菜单URL"
                align="center"
                width="150px">
            </el-table-column>
            <el-table-column
                prop="component"
                label="菜单组件"
                align="center"
                width="150">
            </el-table-column>
            <el-table-column
                prop="state"
                width="80px"
                align="center"
                label="状态">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.state === 1" type="success">正常</el-tag>
                    <el-tag v-if="scope.row.state === 0" type="danger">禁用</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                prop="operation"
                width="135px"
                align="center"
                label="操作">
                <template slot-scope="scope">
                    <el-button type="text" @click="editMenuHandle(scope.row.id)">编辑</el-button>
                    <el-divider direction="vertical"></el-divider>
                    <template>
                        <el-popconfirm
                            title="确人删除吗？"
                            @confirm="deleteMenu(scope.row.id)"
                        >
                            <el-button type="text" slot="reference">删除</el-button>
                        </el-popconfirm>
                    </template>
                </template>
            </el-table-column>
        </el-table>
<!--        点击新增按钮显示表单内容的对话框-->
        <el-dialog
            title="菜单信息"
            :visible.sync="dialogVisible"
            @close="resetForm('editForm')"
            width="35%">
            <el-form :model="editForm" :rules="rules" ref="editForm" label-width="100px" class="demo-editForm">
                <el-form-item label="上级菜单" prop="parentMenuId">
                    <el-select v-model="editForm.parentMenuId" placeholder="请选择上级菜单">
                        <template v-if="item.type==0" v-for="item in tableData">
                            <el-option :label="item.name" :value="item.id"></el-option>
                                <el-option v-for="i in item.children" v-if="i.type==1" :label="i.name" :value="i.id">
                                    <span>-{{i.name}}</span>
                                </el-option>
                        </template>
                    </el-select>
                </el-form-item>

                <el-form-item label="菜单名称" prop="name">
                    <el-input v-model="editForm.name"></el-input>
                </el-form-item>
                <el-form-item label="权限编码" prop="permit">
                    <el-input v-model="editForm.permit"></el-input>
                </el-form-item>
                <el-form-item label="菜单URL" prop="url">
                    <el-input v-model="editForm.url"></el-input>
                </el-form-item>
                <el-form-item label="菜单组件" prop="component">
                    <el-input v-model="editForm.component"></el-input>
                </el-form-item>


                <el-form-item label="类型" prop="type">
                    <el-radio-group v-model="editForm.type">
                        <el-radio :label="0" >目录</el-radio>
                        <el-radio :label="1" >菜单</el-radio>
                        <el-radio :label="2" >按钮</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="状态" prop="state">
                    <el-radio-group v-model="editForm.state">
                        <el-radio :label="1">正常</el-radio>
                        <el-radio :label="0">禁用</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('editForm')">立即创建</el-button>
                    <el-button @click="resetForm('editForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "Menu",
    data() {
        return {
            tableData:[],
            dialogVisible: false,
            editForm: {
                parentMenuId:'',
                name:'',
                url:'',
                component:'',
                state:'',
                permit:'',
                type:''
            },
            rules: {
                name: [
                    { required: true, message: '请输入菜单名称', trigger: 'blur' },
                    { min: 1, max: 8, message: '长度在 1 到 8 个字符', trigger: 'blur' }
                ],
                permit: [
                    { required: true, message: '请选择权限编码', trigger: 'change' }
                ],
                url: [
                    { required: true, message: '请输入菜单URL', trigger: 'blur' },
                ],
                component: [
                    { required: true, message: '请输入菜单组件', trigger: 'blur' },
                ],
                type: [
                    { required: true, message: '请选择菜单类型', trigger: 'change' }
                ],
                state: [
                    { required: true, message: '请选择菜单状态', trigger: 'change' }
                ]
            }
        }
    },
    methods: {
        submitForm(formName) {
            //验证所以表单项是否符合所定义规则
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.addMenu()
                    this.$message({
                        message:'添加成功！',
                        type:"success"
                    })
                } else {
                    console.log('新增内容不合法，请重新输入！');
                    return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
            this.editForm = {}
        },
        getTableData(){
            this.$axios.get('http://localhost:8080/getMenusTableData').then(Response =>{
                this.tableData = Response.data.data.tableData
            })
        },
        //菜单编辑处理，只是显示编辑表单并实现数据回显，后调用
        editMenuHandle(id){

            let targetMenu
            let parentMenuId
            this.tableData.forEach(item =>{
                if (item.id == id) {
                    targetMenu = item
                }else{
                    if (item.hasOwnProperty('children')) {
                        item.children.forEach( e =>{
                            if (e.id == id) {
                                targetMenu = e
                                parentMenuId = item.id
                            }else {
                                if (e.hasOwnProperty('children')) {
                                    e.children.forEach(i =>{
                                        if (i.id == id) {
                                            targetMenu = i
                                            parentMenuId = e.id
                                        }
                                    })
                                }
                            }
                        })
                    }
                }
            })
            this.editForm = {...targetMenu}
            this.editForm.parentMenuId = parentMenuId
            this.dialogVisible = true
            this.editMenu(targetMenu)
        },
        //完成菜单修改
        editMenu(targetMenu){
            //代码略，等后面后端再实现
        },
        //实现新增菜单
        addMenu(){
            //等后面到后端再实现
        },
        //删除菜单
        deleteMenu(id){
            //到时候后端实现
        }
    },
    created() {
        this.getTableData()
    }

}
</script>

<style scoped>

</style>