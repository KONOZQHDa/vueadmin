<template>
    <div>
        <!--        顶部操作栏，用行内表单这样就不用自己再调样式和间距了-->
        <el-form :inline="true" class="demo-form-inline">
            <el-form-item>
                <el-input v-model="searchKey" clearable placeholder="请输入角色名"></el-input>
            </el-form-item>

            <el-form-item>
                <el-button @click="">搜索</el-button>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="dialogVisible = true">新增</el-button>
            </el-form-item>
            <el-form-item>
                <el-popconfirm
                    title="确人删除吗？"
                    @confirm="deleteRoles"
                >
                    <el-button type="danger"
                               slot="reference"
                               :disabled="deleteButtonDisabled"
                               v-if="hasAuthorization('sys:role:delete')">
                        批量删除</el-button>
                </el-popconfirm>
            </el-form-item>
        </el-form>
        <!--        角色信息展示表格-->
        <el-table
            ref="multipleTable"
            :data="tableData"
            tooltip-effect="dark"
            style="width: 100%"
            border
            @selection-change="handleSelectionChange">
            <el-table-column
                align="center"
                type="selection"
                width="50">
            </el-table-column>
            <el-table-column
                label="名称"
                align="center"
                prop="roleName"
                width="120">
            </el-table-column>
            <el-table-column
                prop="roleCode"
                align="center"
                label="唯一编码"
                width="150">
            </el-table-column>
            <el-table-column
                prop="description"
                align="center"
                width="300px"
                label="描述">
            </el-table-column>
            <el-table-column
                align="center"
                width="90px"
                label="状态">
                <template slot-scope="scope">
                    <el-tag type="success" v-if="scope.row.state == 1">正常</el-tag>
                    <el-tag type="danger" v-if="scope.row.state == 0">禁用</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                align="center"
                width="260px"
                label="操作">
                <template slot-scope="scope">
                    <el-button type="text" @click="assignPermissions(scope.row.roleCode)">分配权限</el-button>
                    <el-divider direction="vertical"></el-divider>

                    <el-button type="text" @click="editRole(scope.row.roleCode)">编辑</el-button>
                    <el-divider direction="vertical"></el-divider>

                    <el-popconfirm
                        title="确人删除吗？"
                        @confirm="deleteRoles"
                    >
                        <el-button type="text" slot="reference">删除</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <!--        分页-->
        <div style="display: flex;flex-direction: row; margin-top: 18px;">
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[10, 20, 30, 40]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
            </el-pagination>
        </div>
        <!--        点击新增后弹出的对话框-->
        <el-dialog
            title="角色信息"
            :visible.sync="dialogVisible"
            @close="resetForm('editForm')"
            width="35%">
            <el-form :model="editForm" :rules="rules" ref="editForm" label-width="100px" class="demo-editForm">

                <el-form-item label="角色名称" prop="roleName">
                    <el-input v-model="editForm.roleName"></el-input>
                </el-form-item>
                <el-form-item label="唯一编码" prop="roleCode">
                    <el-input v-model="editForm.roleCode"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input v-model="editForm.description"></el-input>
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
        <!--        分配权限按钮点击后显示的对话框-->
        <el-dialog
            title="分配权限"
            :visible.sync="dialogVisible2"
            @close=""
            width="35%">
            <el-tree
                :data="treeData"
                show-checkbox
                node-key="id"
                ref="tree"
                check-strictly
                :default-expand-all="true"
                :props="defaultProps">
            </el-tree>
            <span slot="footer" class="dialog-footer" style="display: flex;flex-direction: row">
                <el-button @click="dialogVisible2 = false">取 消</el-button>
                <el-button type="primary" @click="assignPermissionsHandle">确 定</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
export default {
    name: "Role",
    data() {
        return {
            searchKey: '',
            selectedRoleCode: '',
            dialogVisible: false,
            dialogVisible2: false,
            currentPage: 1,
            total: 2,
            pageSize: 10,
            tableData: [],
            // 表格中选中的对象数组
            multipleSelection: [],
            editForm: {
                roleName: '',
                roleCode: '',
                description: '',
                state: ''
            },
            rules: {
                roleName: [
                    {required: true, message: '请输入角色名称', trigger: 'blur'},
                ],
                roleCode: [
                    {required: true, message: '请输入角色唯一编码', trigger: 'blur'},
                ],
                state: [
                    {required: true, message: '请输入角色状态', trigger: 'blur'},
                ],
                description: [
                    {required: true, message: '请输入角色描述', trigger: 'blur'},
                ],
            },
            // 树形控件的数据
            treeData: [],
            //树形控件配置对象中作为子树的属性的名字，还有哪个属性作为标签显示
            defaultProps: {
                children: 'children',
                label: 'name'
            },
        }
    },
    methods: {
        //编辑按钮click事件回调函数,只完成显示对话框及其数据回显
        editRole(roleCode) {
            this.tableData.forEach(item => {
                if (item.roleCode === roleCode) {
                    this.editForm = item
                }
            })
            this.dialogVisible = true
            //将改行的角色设置为被选中
            this.selectedRoleCode = roleCode
        },
        //发生请求处理角色修改
        handleEditRole(roleCode){

        },
        deleteRoles() {

        },
        //以下为组件自带函数：
        toggleSelection(rows) {
            if (rows) {
                rows.forEach(row => {
                    this.$refs.multipleTable.toggleRowSelection(row);
                });
            } else {
                this.$refs.multipleTable.clearSelection();
            }
        },
        //表格自带方法，勾选内容改变后回调
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        //分页组件自带函数
        handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
        },
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
            this.editForm = {}
        },
        submitForm(formName) {
            //验证所以表单项是否符合所定义规则
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.$message({
                        message: '操作成功！',
                        type: "success"
                    })
                } else {
                    this.$message({
                        message: '内容不符合要求，请重新输入！',
                        type: "warning"
                    })
                    return false;
                }
            });
        },
        getRoles() {
            this.$axios.get('http://localhost:8080/getRolesTableData').then(Response => {
                this.tableData = Response.data.data.tableData
                this.total = Response.data.data.total
            })
        },
        getTreeData() {
            this.$axios.get('http://localhost:8080/getMenusTableData').then(Response => {
                this.treeData = Response.data.data.tableData
            })
        },
        //分配权限click事件回调函数,只完成显示对话框及其数据回显，真正的是表单提交assignPermissionsHandle方法响应到后端
        assignPermissions(roleCode) {
            this.dialogVisible2 = true
            this.$nextTick(() => {
                this.$axios.get('http://localhost:8080/getMenusTableData').then(Response => {
                    this.$refs.tree.setCheckedKeys(Response.data.data.idsArray)
                })
            })
            //将改行的角色设置为被选中
            this.selectedRoleCode = roleCode
        },
        //发生请求进行权限修改
        assignPermissionsHandle(roleCode){
            //拿到树形控件中选中项的id值数组
            let checkedKeys = this.$refs.tree.getCheckedKeys();
            this.$message({
                message: '操作成功！',
                type : 'success'
            })
        }
    },
    computed: {
        deleteButtonDisabled() {
            return this.multipleSelection.length == 0 ? true : false
        }
    },
    created() {
        this.getRoles()
        this.getTreeData()
    }
}
</script>

<style scoped>

</style>