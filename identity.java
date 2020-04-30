public class UserController extends BaseController {
    private static final String DEFAULT_PSW = "123456";  // �û�Ĭ������
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('get:/v1/user')")
    @GetMapping()
    public PageResult<User> list(HttpServletRequest request) {
        return userService.listUser(new PageParam(request).setDefaultOrder(new String[]{"create_time"}, null));
    }

    @PreAuthorize("hasAuthority('post:/v1/user')")
    @PostMapping()
    public JsonResult add(User user, String roleIds) {
        user.setState(0);
        user.setPassword(passwordEncoder.encode(DEFAULT_PSW));
        if (userService.addUser(user, getRoleIds(roleIds))) {
            return JsonResult.ok("��ӳɹ�����ʼ����Ϊ" + DEFAULT_PSW);
        }
        return JsonResult.error("���ʧ��");
    }

    @PreAuthorize("hasAuthority('put:/v1/user')")
    @PutMapping()
    public JsonResult update(User user, String roleIds) {
        if (userService.updateUser(user, getRoleIds(roleIds))) {
            return JsonResult.ok("�޸ĳɹ�");
        }
        return JsonResult.error("�޸�ʧ��");
    }

    @PreAuthorize("hasAuthority('put:/v1/user/state')")
    @PutMapping("/state")
    public JsonResult updateState(Integer userId, Integer state) {
        if (userId == null) {
            return JsonResult.error("����userId����Ϊ��");
        }
        if (state == null || (state != 0 && state != 1)) {
            return JsonResult.error("״ֵ̬����ȷ");
        }
        User user = new User();
        user.setUserId(userId);
        user.setState(state);
        if (userService.updateById(user)) {
            return JsonResult.ok();
        }
        return JsonResult.error();
    }

    @PreAuthorize("hasAuthority('put:/v1/user/psw')")
    @PutMapping("/psw")
    public JsonResult updatePsw(String oldPsw, String newPsw) {
        if (StringUtil.isBlank(oldPsw, newPsw)) {
            return JsonResult.error("��������Ϊ��");
        }
        if (getLoginUser() == null) {
            return JsonResult.error("δ��¼");
        }
        if (!passwordEncoder.matches(oldPsw, getLoginUser().getPassword())) {
            return JsonResult.error("ԭ�������벻��ȷ");
        }
        User user = new User();
        user.setUserId(getLoginUser().getUserId());
        user.setPassword(passwordEncoder.encode(newPsw));
        if (userService.updateById(user)) {
            return JsonResult.ok("�޸ĳɹ�");
        }
        return JsonResult.error("�޸�ʧ��");
    }

    @PreAuthorize("hasAuthority('put:/v1/user/psw/{id}')")
    @PutMapping("/psw/{id}")
    public JsonResult resetPsw(@PathVariable("id") Integer userId) {
        if (userId == null) {
            return JsonResult.error("����userId����Ϊ��");
        }
        User user = new User();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(DEFAULT_PSW));
        if (userService.updateById(user)) {
            return JsonResult.ok("���óɹ�����ʼ����Ϊ" + DEFAULT_PSW);
        } else {
            return JsonResult.error("����ʧ��");
        }
    }

    @PreAuthorize("hasAuthority('delete:/v1/user/{id}')")
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable("id") Integer userId) {
        if (userId == null) {
            return JsonResult.error("����userId����Ϊ��");
        }
        if (userService.removeById(userId)) {
            return JsonResult.ok("ɾ���ɹ�");
        } else {
            return JsonResult.error("ɾ��ʧ��");
        }
    }

    /**
     * �ö��ŷָ��ɫ
     */
    private List<Integer> getRoleIds(String rolesStr) {
        List<Integer> roleIds = new ArrayList<>();
        if (rolesStr != null) {
            String[] split = rolesStr.split(",");
            for (String t : split) {
                try {
                    roleIds.add(Integer.parseInt(t));
                } catch (Exception e) {
                }
            }
        }
        return roleIds;
    }
}